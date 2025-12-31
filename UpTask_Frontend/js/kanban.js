const API = "http://localhost:8080/api/tasks";
const token = localStorage.getItem("token");
const role = localStorage.getItem("role");

document.getElementById("roleTitle").innerText = role + " Dashboard";

let tasks = [];

// ===== FETCH TASKS =====
async function loadTasks() {
    const res = await fetch(API, {
        headers: {
            Authorization: "Bearer " + token
        }
    });

    tasks = await res.json();
    render();
}

// ===== RENDER KANBAN =====
function render() {
    document.querySelectorAll(".kanban-list").forEach(l => l.innerHTML = "");

    tasks.forEach(t => {
        const card = document.createElement("div");
        card.className = "kanban-card";
        card.draggable = true;
        card.dataset.id = t.id;

        card.innerHTML = `
            <h4>${t.title}</h4>
            <p>${t.description || ""}</p>
            <small>Status: ${t.status}</small>
            ${role === "ADMIN" ? `
                <button onclick="deleteTask(${t.id})">🗑 Delete</button>
            ` : ""}
        `;

        document
            .querySelector(`[data-status="${t.status}"]`)
            .appendChild(card);
    });

    enableDragDrop();
}

// ===== DRAG & DROP =====
function enableDragDrop() {
    const cards = document.querySelectorAll(".kanban-card");
    const lists = document.querySelectorAll(".kanban-list");

    cards.forEach(card => {
        card.addEventListener("dragstart", () => card.classList.add("dragging"));
        card.addEventListener("dragend", () => card.classList.remove("dragging"));
    });

    lists.forEach(list => {
        list.addEventListener("dragover", async e => {
            e.preventDefault();
            const card = document.querySelector(".dragging");
            if (!card) return;

            const taskId = card.dataset.id;
            const newStatus = list.dataset.status;

            list.appendChild(card);
            await updateStatus(taskId, newStatus);
        });
    });
}

// ===== UPDATE STATUS =====
async function updateStatus(id, status) {
    await fetch(`${API}/${id}/status`, {
        method: "PATCH",
        headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + token
        },
        body: JSON.stringify({ status })
    });
}

// ===== DELETE =====
async function deleteTask(id) {
    if (!confirm("Delete this task?")) return;

    await fetch(`${API}/${id}`, {
        method: "DELETE",
        headers: {
            Authorization: "Bearer " + token
        }
    });

    tasks = tasks.filter(t => t.id !== id);
    render();
}

loadTasks();
