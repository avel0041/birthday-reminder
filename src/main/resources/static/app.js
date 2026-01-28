// Generated with the assistance of DeepSeek AI
class BirthdayApp {
    constructor() {
        this.API_BASE_URL = '/api/birthdays';
        this.init();
    }

    async init() {
        await this.loadDashboard();
        await this.loadAllBirthdays();
        this.setupEventListeners();
    }

    setupEventListeners() {
        // Форма добавления
        document.getElementById('birthdayForm').addEventListener('submit', async (e) => {
            e.preventDefault();
            await this.createBirthday();
        });
        
        // Инициализация модального окна редактирования
        this.setupEditModal();
    }
    
    setupEditModal() {
        // Очищаем форму при закрытии модального окна
        const editModal = document.getElementById('editModal');
        if (editModal) {
            editModal.addEventListener('hidden.bs.modal', () => {
                document.getElementById('editForm').reset();
                document.getElementById('editId').value = '';
            });
        }
    }

    async loadDashboard() {
        try {
            const response = await fetch(`${this.API_BASE_URL}/dashboard`);
            if (!response.ok) throw new Error('Ошибка загрузки dashboard');
            
            const data = await response.json();
            
            this.renderTodayBirthdays(data.todayBirthdays);
            this.renderUpcomingBirthdays(data.upcomingBirthdays);
            
            // Обновляем заголовки с количеством
            const todayHeader = document.querySelector('.card-header.bg-success h5');
            const upcomingHeader = document.querySelector('.card-header.bg-primary h5');
            
            if (todayHeader) {
                todayHeader.innerHTML = `🎉 Сегодня празднуют (${data.todayCount})`;
            }
            if (upcomingHeader) {
                upcomingHeader.innerHTML = `📅 Ближайшие дни рождения (${data.upcomingCount})`;
            }
            
        } catch (error) {
            console.error('Ошибка загрузки dashboard:', error);
            this.showError('todayBirthdays', 'Ошибка загрузки данных');
            this.showError('upcomingBirthdays', 'Ошибка загрузки данных');
        }
    }

    async loadAllBirthdays() {
        try {
            const response = await fetch(this.API_BASE_URL);
            if (!response.ok) throw new Error('Ошибка загрузки всех дней рождений');
            
            const birthdays = await response.json();
            this.renderAllBirthdays(birthdays);
        } catch (error) {
            console.error('Ошибка загрузки всех дней рождений:', error);
            this.showError('allBirthdays', 'Ошибка загрузки данных');
        }
    }

    getYearsWord(years) {
        const lastDigit = years % 10;
        const lastTwoDigits = years % 100;
        
        if (lastTwoDigits >= 11 && lastTwoDigits <= 14) {
            return 'лет';
        }

        switch (lastDigit) {
            case 1:
                return 'год';
            case 2:
            case 3:
            case 4:
                return 'года';
            default:
                return 'лет';
        }
    }

    renderTodayBirthdays(birthdays) {
        const container = document.getElementById('todayBirthdays');
        
        if (!birthdays || birthdays.length === 0) {
            container.innerHTML = `
                <div class="alert alert-info">
                    Сегодня нет дней рождений 😊
                </div>
            `;
            return;
        }

        container.innerHTML = birthdays.map(birthday => `
            <div class="card birthday-card today-birthday mb-3">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-start">
                        <div>
                            <h5 class="card-title mb-1">${birthday.firstName} ${birthday.lastName}</h5>
                            <p class="card-text mb-1">
                                <small class="text-muted">
                                    Дата рождения: ${this.formatDate(birthday.birthDate)}
                                </small>
                            </p>
                            <p class="card-text mb-0">
                                <span class="badge bg-success birthday-badge">
                                    Исполняется ${birthday.age} ${this.getYearsWord(birthday.age)}
                                </span>
                            </p>
                        </div>
                    </div>
                </div>    
            </div>
        `).join('');
    }

    renderUpcomingBirthdays(birthdays) {
        const container = document.getElementById('upcomingBirthdays');
        
        if (!birthdays || birthdays.length === 0) {
            container.innerHTML = `
                <div class="alert alert-info">
                    Ближайшие дни рождения отсутствуют
                </div>
            `;
            return;
        }

        container.innerHTML = birthdays.map(birthday => `
            <div class="upcoming-item">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <strong>${birthday.firstName} ${birthday.lastName}</strong>
                        <div class="text-muted small">
                            ${this.formatDate(birthday.nextBirthday, { month: 'long', day: 'numeric' })}
                        </div>
                    </div>
                    <div>
                        <span class="badge bg-primary">
                            Через ${birthday.daysUntilBirthday} дней
                        </span>
                        <span class="badge bg-secondary ms-1">
                            ${birthday.age + 1} лет
                        </span>
                    </div>
                </div>
            </div>
        `).join('');
    }

    renderAllBirthdays(birthdays) {
        const container = document.getElementById('allBirthdays');
        
        if (!birthdays || birthdays.length === 0) {
            container.innerHTML = `
                <div class="alert alert-info">
                    Нет добавленных дней рождений
                </div>
            `;
            return;
        }

        container.innerHTML = `
            <div class="table-responsive">
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th>Имя</th>
                            <th>Дата рождения</th>
                            <th>Возраст</th>
                            <th>Следующий ДР</th>
                            <th>Действия</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${birthdays.map(birthday => `
                            <tr ${birthday.birthdayToday ? 'class="table-success"' : ''}>
                                <td>
                                    <strong>${birthday.firstName} ${birthday.lastName}</strong>
                                    ${birthday.birthdayToday ? 
                                        '<span class="badge bg-success ms-1">Сегодня!</span>' : 
                                        ''}
                                </td>
                                <td>${this.formatDate(birthday.birthDate)}</td>
                                <td>${birthday.age}</td>
                                <td>
                                    ${this.formatDate(birthday.nextBirthday)}
                                    ${birthday.daysUntilBirthday > 0 ? 
                                        `<span class="badge bg-info">через ${birthday.daysUntilBirthday} дн.</span>` : 
                                        ''}
                                </td>
                                <td>
                                    <button class="btn btn-sm btn-outline-primary" 
                                            onclick="app.editBirthday(${birthday.id}, '${birthday.firstName}', '${birthday.lastName}', '${birthday.birthDate}')">
                                        ✏️
                                    </button>
                                    <button class="btn btn-sm btn-outline-danger ms-1" 
                                            onclick="app.deleteBirthday(${birthday.id})">
                                        🗑️
                                    </button>
                                </td>
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            </div>
        `;
    }

    async createBirthday() {
        const formData = {
            firstName: document.getElementById('firstName').value.trim(),
            lastName: document.getElementById('lastName').value.trim(),
            birthDate: document.getElementById('birthDate').value
        };

        // Валидация
        if (!formData.firstName || !formData.lastName || !formData.birthDate) {
            this.showToast('Заполните обязательные поля (Имя, Фамилия, Дата рождения)', 'warning');
            return;
        }

        try {
            const response = await fetch(this.API_BASE_URL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify(formData)
            });

            if (response.ok) {
                document.getElementById('birthdayForm').reset();
                await this.loadDashboard();
                await this.loadAllBirthdays();
                this.showToast('День рождения успешно добавлен!', 'success');
            } else {
                const errorText = await response.text();
                console.error('Ошибка сервера:', errorText);
                this.showToast('Ошибка при добавлении: ' + (errorText || 'Неизвестная ошибка'), 'danger');
            }
        } catch (error) {
            console.error('Ошибка создания:', error);
            this.showToast('Ошибка сети при добавлении', 'danger');
        }
    }

    // ВАЖНО: Изменяем метод editBirthday - передаем данные напрямую, а не загружаем с сервера
    editBirthday(id, firstName, lastName, birthDate) {
        // Экранируем кавычки в строках
        const escapeQuotes = (str) => (str || '').replace(/'/g, "\\'").replace(/"/g, '\\"');
        
        // Заполняем форму редактирования
        document.getElementById('editId').value = id;
        document.getElementById('editFirstName').value = escapeQuotes(firstName);
        document.getElementById('editLastName').value = escapeQuotes(lastName);
        document.getElementById('editBirthDate').value = birthDate;
        
        // Показываем модальное окно
        const modalElement = document.getElementById('editModal');
        const modal = new bootstrap.Modal(modalElement);
        modal.show();
    }

    async updateBirthday() {
        const id = document.getElementById('editId').value;
        
        // Проверяем ID
        if (!id || id === 'undefined') {
            this.showToast('Ошибка: ID не определен', 'danger');
            return;
        }

        const formData = {
            firstName: document.getElementById('editFirstName').value.trim(),
            lastName: document.getElementById('editLastName').value.trim(),
            birthDate: document.getElementById('editBirthDate').value
        };

        // Валидация
        if (!formData.firstName || !formData.lastName || !formData.birthDate) {
            this.showToast('Заполните обязательные поля (Имя, Фамилия, Дата рождения)', 'warning');
            return;
        }

        try {
            const response = await fetch(`${this.API_BASE_URL}/${id}`, {
                method: 'PUT',  // Используем PUT метод
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify(formData)
            });

            if (response.ok) {
                // Закрываем модальное окно
                const modal = bootstrap.Modal.getInstance(document.getElementById('editModal'));
                modal.hide();
                
                // Обновляем данные
                await this.loadDashboard();
                await this.loadAllBirthdays();
                this.showToast('День рождения успешно обновлен!', 'success');
            } else {
                const errorText = await response.text();
                console.error('Ошибка сервера:', errorText);
                this.showToast('Ошибка при обновлении: ' + (errorText || 'Неизвестная ошибка'), 'danger');
            }
        } catch (error) {
            console.error('Ошибка обновления:', error);
            this.showToast('Ошибка сети при обновлении', 'danger');
        }
    }

    async deleteBirthday(id) {
        if (!id || id === 'undefined') {
            this.showToast('Ошибка: ID не определен', 'danger');
            return;
        }

        if (!confirm('Вы уверены, что хотите удалить этот день рождения?')) {
            return;
        }

        try {
            const response = await fetch(`${this.API_BASE_URL}/${id}`, {
                method: 'DELETE'
            });

            if (response.ok) {
                await this.loadDashboard();
                await this.loadAllBirthdays();
                this.showToast('День рождения удален', 'warning');
            } else {
                throw new Error('Ошибка при удалении');
            }
        } catch (error) {
            console.error('Ошибка удаления:', error);
            this.showToast('Ошибка при удалении', 'danger');
        }
    }

    formatDate(dateString, options = {}) {
        if (!dateString) return '';
        
        const date = new Date(dateString);
        const defaultOptions = { 
            year: 'numeric', 
            month: 'short', 
            day: 'numeric' 
        };
        
        return date.toLocaleDateString('ru-RU', { ...defaultOptions, ...options });
    }

    showError(containerId, message) {
        const container = document.getElementById(containerId);
        if (container) {
            container.innerHTML = `
                <div class="alert alert-danger">
                    ${message}
                    <button class="btn btn-sm btn-outline-danger ms-2" 
                            onclick="location.reload()">
                        Обновить страницу
                    </button>
                </div>
            `;
        }
    }

    showToast(message, type = 'info') {
        document.querySelectorAll('.alert-toast').forEach(toast => toast.remove());
        
        const toast = document.createElement('div');
        toast.className = `alert-toast position-fixed top-0 end-0 m-3 alert alert-${type} alert-dismissible fade show`;
        toast.style.zIndex = '9999';
        toast.innerHTML = `
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;
        
        document.body.appendChild(toast);
        
        setTimeout(() => {
            if (toast.parentNode) {
                toast.remove();
            }
        }, 5000);
    }
}

const app = new BirthdayApp();

window.app = app;
window.updateBirthday = () => app.updateBirthday();