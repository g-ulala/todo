import './style.css'

interface TodoItem {
  id: number;
  text: string;
  isChecked: boolean;
}

const API_URL = '/api/todos';

const todoListElement = document.querySelector<HTMLUListElement>('#todoList')!;
const todoInput = document.querySelector<HTMLInputElement>('#todoInput')!;
const addButton = document.querySelector<HTMLButtonElement>('#addButton')!;

async function fetchTodos(): Promise<void> {
  try {
    const response = await fetch(API_URL);
    if (!response.ok) throw new Error('Failed to fetch todos');
    const todos: TodoItem[] = await response.json();
    renderTodos(todos);
  } catch (error) {
    console.error(error);
  }
}

function renderTodos(todos: TodoItem[]): void {
  todoListElement.innerHTML = '';
  todos.forEach(todo => {
    const li = document.createElement('li');
    if (todo.isChecked) li.classList.add('completed');

    const checkbox = document.createElement('input');
    checkbox.type = 'checkbox';
    checkbox.checked = todo.isChecked;
    checkbox.addEventListener('change', () => toggleTodo(todo));

    const span = document.createElement('span');
    span.textContent = todo.text;

    const deleteBtn = document.createElement('button');
    deleteBtn.textContent = '削除';
    deleteBtn.className = 'delete-btn';
    deleteBtn.addEventListener('click', () => deleteTodo(todo.id));

    li.appendChild(checkbox);
    li.appendChild(span);
    li.appendChild(deleteBtn);
    todoListElement.appendChild(li);
  });
}

async function addTodo(): Promise<void> {
  const text = todoInput.value.trim();
  if (!text) return;

  try {
    await fetch(API_URL, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ text })
    });
    todoInput.value = '';
    fetchTodos();
  } catch (error) {
    console.error('Error adding todo:', error);
  }
}

async function toggleTodo(todo: TodoItem): Promise<void> {
  try {
    await fetch(`${API_URL}/${todo.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ ...todo, isChecked: !todo.isChecked })
    });
    fetchTodos();
  } catch (error) {
    console.error('Error toggling todo:', error);
  }
}

async function deleteTodo(id: number): Promise<void> {
  if (!confirm('本当に削除しますか？')) {
    return;
  }

  try {
    await fetch(`${API_URL}/${id}`, { method: 'DELETE' });
    fetchTodos();
  } catch (error) {
    console.error('Error deleting todo:', error);
  }
}

// Event Listeners
addButton.addEventListener('click', addTodo);
todoInput.addEventListener('keypress', (e) => {
  if (e.key === 'Enter') addTodo();
});

// Initial load
fetchTodos();

function showErrorModal(message: string): void {
  const overlay = document.createElement('div');
  overlay.className = 'modal-overlay';

  const modal = document.createElement('div');
  modal.className = 'modal';

  const h2 = document.createElement('h2');
  h2.textContent = 'エラー';

  const p = document.createElement('p');
  p.textContent = message;

  const button = document.createElement('button');
  button.className = 'modal-btn';
  button.textContent = 'OK';

  modal.appendChild(h2);
  modal.appendChild(p);
  modal.appendChild(button);

  overlay.appendChild(modal);
  document.body.appendChild(overlay);

  const closeModal = () => {
    overlay.classList.add('fade-out');
    setTimeout(() => overlay.remove(), 300);
  };

  modal.querySelector('.modal-btn')!.addEventListener('click', closeModal);
  overlay.addEventListener('click', (e) => {
    if (e.target === overlay) closeModal();
  });
}