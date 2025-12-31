const token = localStorage.getItem("token");

const res = await fetch("http://localhost:8080/api/tasks", {
    method: "GET",
    headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
    }
});

const data = await res.json();
