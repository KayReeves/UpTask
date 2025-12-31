// Check auth on page load
(function checkAuth() {
    const token = localStorage.getItem("token");
    if (!token) {
        window.location.href = "login.html";
    }
})();

// Set user info on page load
document.addEventListener("DOMContentLoaded", () => {
    const userName = localStorage.getItem("userName") || "User";
    const userRole = localStorage.getItem("role") || "STAFF";
    document.getElementById("userInfo").innerText = `Welcome, ${userRole}`;
    
    // Load data
    fetchTasks();
    fetchUsers();
});

// Show/hide sections
function showSection(sectionId) {
    document.querySelectorAll(".dashboard-section").forEach(sec => {
        sec.classList.add("hidden");
    });
    const section = document.getElementById(sectionId);
    if (section) {
        section.classList.remove("hidden");
    }
}

// Fetch tasks from backend
async function fetchTasks() {
    const token = localStorage.getItem("token");
    const tasksList = document.getElementById("tasksList");
    
    if (!tasksList) return;
    
    tasksList.innerHTML = "<p>Loading tasks...</p>";
    
    try {
        const res = await fetch("http://localhost:8080/api/tasks", {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            }
        });
        
        if (!res.ok) {
            throw new Error(`HTTP error! status: ${res.status}`);
        }
        
        const tasks = await res.json();
        tasksList.innerHTML = "";
        
        if (!Array.isArray(tasks) || tasks.length === 0) {
            tasksList.innerHTML = "<p>No tasks found.</p>";
            return;
        }
        
        tasks.forEach(task => {
            const card = document.createElement("div");
            card.className = "card";
            card.innerHTML = `
                <h4>${task.title || "Untitled"}</h4>
                <p>Status: ${task.status || "N/A"}</p>
                <p>Priority: ${task.priority || "N/A"}</p>
            `;
            tasksList.appendChild(card);
        });
    } catch (err) {
        console.error("Error fetching tasks:", err);
        tasksList.innerHTML = "<p>Error loading tasks. Check console for details.</p>";
    }
}

// Fetch users from backend
async function fetchUsers() {
    const token = localStorage.getItem("token");
    const usersList = document.getElementById("usersList");
    
    if (!usersList) return;
    
    usersList.innerHTML = "<p>Loading users...</p>";
    
    try {
        const res = await fetch("http://localhost:8080/api/users", {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            }
        });
        
        if (!res.ok) {
            throw new Error(`HTTP error! status: ${res.status}`);
        }
        
        const users = await res.json();
        usersList.innerHTML = "";
        
        if (!Array.isArray(users) || users.length === 0) {
            usersList.innerHTML = "<p>No users found.</p>";
            return;
        }
        
        users.forEach(user => {
    const row = document.createElement("div");
    row.className = "user-row";

    row.innerHTML = `
        <span>${user.name}</span>
        <span>${user.email}</span>
        <span>${user.role}</span>
    `;

    usersList.appendChild(row);
});

    } catch (err) {
        console.error("Error fetching users:", err);
        usersList.innerHTML = "<p>Error loading users. Check console for details.</p>";
    }
}

const role = localStorage.getItem("role");
const userInfo = document.getElementById("userInfo");

userInfo.innerText = localStorage.getItem("userName") + " (" + role + ")";

if (role === "ADMIN") {
    document.getElementById("taskFormContainer").innerHTML = `
        <h4>Create Task</h4>
        <input id="title" placeholder="Title">
        <input id="description" placeholder="Description">
        <select id="status">
            <option value="TODO">TODO</option>
            <option value="IN_PROGRESS">IN_PROGRESS</option>
            <option value="DONE">DONE</option>
        </select>
        <button onclick="createTask()">Create</button>
    `;
}

async function createTask() {
    const title = document.getElementById("title").value;
    const description = document.getElementById("description").value;
    const status = document.getElementById("status").value;

    await fetch("http://localhost:8080/api/tasks/add", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + localStorage.getItem("token")
        },
        body: JSON.stringify({ title, description, status })
    });

    location.reload();
}

