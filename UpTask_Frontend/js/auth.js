// ===== AUTH GUARD =====
(function checkAuth() {
    const token = localStorage.getItem("token");
    const path = window.location.pathname;

    const isLoginPage = path.endsWith("login.html");
    const isIndexPage = path.endsWith("index.html");

    if (!token && !isLoginPage && !isIndexPage) {
        window.location.href = "/html/login.html";
    }
})();

// ===== LOGIN =====
const loginForm = document.getElementById("loginForm");
if (loginForm) {
    loginForm.addEventListener("submit", login);
}

async function login(e) {
    e.preventDefault();

    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();
    const errorDiv = document.getElementById("errorMessage");

    if (!email || !password) {
        errorDiv.innerText = "Please fill in all fields.";
        return;
    }

    errorDiv.innerText = "";

    try {
        const res = await fetch("http://localhost:8080/api/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password })
        });

        if (!res.ok) {
            errorDiv.innerText = res.status === 401
                ? "Invalid email or password."
                : "Server error. Try again.";
            return;
        }

        const user = await res.json();

        localStorage.setItem("token", user.token);
        localStorage.setItem("userId", user.id);
        localStorage.setItem("role", user.role);
        localStorage.setItem("userName", user.name || user.email);

        window.location.href = "/html/dashboard.html";

    } catch (err) {
        console.error(err);
        errorDiv.innerText = "Network error.";
    }
}

// ===== LOGOUT =====
function logout() {
    localStorage.clear();
    window.location.href = "/html/login.html";
}
