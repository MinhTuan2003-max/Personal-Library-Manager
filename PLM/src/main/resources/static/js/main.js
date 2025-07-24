'use strict';

const BookShelfApp = {
    config: {
        animationDuration: 300,
        debounceDelay: 300,
        autoHideAlerts: 5000,
        maxFileSize: 10 * 1024 * 1024, // 10MB
        allowedImageTypes: ['image/jpeg', 'image/jpg', 'image/png', 'image/gif']
    },

    init() {
        this.setupEventListeners();
        this.initializeComponents();
        this.setupFormValidation();
        this.setupImagePreview();
        this.setupNumberFormatting();
        this.setupSearchFeatures();
        this.setupLoadingStates();
        this.setupThemeToggle();
        console.log('BookShelf App initialized successfully');
    },

    setupEventListeners() {
        document.addEventListener('DOMContentLoaded', () => {
            this.autoHideAlerts();
            this.setupSmoothScrolling();
            this.setupTooltips();
            this.setupDropdowns();
        });

        document.addEventListener('visibilitychange', () => {
            if (document.hidden) {
                console.log('Page hidden - pausing non-critical operations');
            } else {
                console.log('Page visible - resuming operations');
            }
        });

        window.addEventListener('online', () => {
            this.showToast('Kết nối internet đã được khôi phục', 'success');
        });

        window.addEventListener('offline', () => {
            this.showToast('Mất kết nối internet', 'warning');
        });
    },

    initializeComponents() {
        const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
        tooltipTriggerList.map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl));

        const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
        popoverTriggerList.map(popoverTriggerEl => new bootstrap.Popover(popoverTriggerEl));

        const modalElements = document.querySelectorAll('.modal');
        modalElements.forEach(modalEl => {
            modalEl.addEventListener('shown.bs.modal', () => {
                const firstInput = modalEl.querySelector('input, textarea, select');
                if (firstInput) firstInput.focus();
            });
        });
    },

    autoHideAlerts() {
        const alerts = document.querySelectorAll('.alert:not(.alert-permanent)');
        alerts.forEach(alert => {
            setTimeout(() => {
                const bsAlert = bootstrap.Alert.getOrCreateInstance(alert);
                if (bsAlert) {
                    bsAlert.close();
                }
            }, this.config.autoHideAlerts);
        });
    },

    setupFormValidation() {
        const forms = document.querySelectorAll('.needs-validation');
        forms.forEach(form => {
            form.addEventListener('submit', event => {
                if (!form.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();
                    this.showFirstValidationError(form);
                } else {
                    this.showLoadingState(form);
                }
                form.classList.add('was-validated');
            });

            const inputs = form.querySelectorAll('input, select, textarea');
            inputs.forEach(input => {
                input.addEventListener('blur', () => this.validateField(input));
                input.addEventListener('input', () => this.clearFieldErrors(input));
            });
        });
    },

    validateField(field) {
        const isValid = field.checkValidity();
        field.classList.toggle('is-invalid', !isValid);
        field.classList.toggle('is-valid', isValid);
        return isValid;
    },

    clearFieldErrors(field) {
        field.classList.remove('is-invalid', 'is-valid');
    },

    showFirstValidationError(form) {
        const firstInvalidField = form.querySelector('.is-invalid, :invalid');
        if (firstInvalidField) {
            firstInvalidField.focus();
            firstInvalidField.scrollIntoView({ behavior: 'smooth', block: 'center' });
        }
    },

    setupImagePreview() {
        const imageInputs = document.querySelectorAll('input[type="file"][accept*="image"]');
        imageInputs.forEach(input => {
            input.addEventListener('change', event => this.handleImagePreview(event));
        });
    },

    handleImagePreview(event) {
        const file = event.target.files[0];
        const input = event.target;

        if (!file) return;

        if (!this.config.allowedImageTypes.includes(file.type)) {
            this.showToast('Chỉ chấp nhận file ảnh (JPG, PNG, GIF)', 'error');
            input.value = '';
            return;
        }

        if (file.size > this.config.maxFileSize) {
            this.showToast('File quá lớn. Kích thước tối đa là 10MB', 'error');
            input.value = '';
            return;
        }

        const reader = new FileReader();
        reader.onload = e => {
            let preview = document.getElementById('coverPreview') ||
                input.parentNode.querySelector('.image-preview');

            if (!preview) {
                preview = this.createImagePreview(input);
            }

            if (preview.tagName === 'IMG') {
                preview.src = e.target.result;
                preview.style.display = 'block';
            } else {
                preview.innerHTML = `<img src="${e.target.result}" class="img-thumbnail" style="max-width: 200px; max-height: 300px;" alt="Preview">`;
            }

            this.addImageRemoveButton(preview, input);
        };

        reader.onerror = () => {
            this.showToast('Không thể đọc file ảnh', 'error');
        };

        reader.readAsDataURL(file);
    },

    createImagePreview(input) {
        const preview = document.createElement('div');
        preview.className = 'image-preview mt-3';
        input.parentNode.appendChild(preview);
        return preview;
    },

    addImageRemoveButton(preview, input) {
        let removeBtn = preview.parentNode.querySelector('.remove-image-btn');
        if (!removeBtn) {
            removeBtn = document.createElement('button');
            removeBtn.type = 'button';
            removeBtn.className = 'btn btn-sm btn-danger remove-image-btn mt-2';
            removeBtn.innerHTML = '<i class="fas fa-times me-1"></i>Xóa ảnh';
            removeBtn.addEventListener('click', () => {
                input.value = '';
                preview.style.display = 'none';
                removeBtn.remove();
            });
            preview.parentNode.appendChild(removeBtn);
        }
    },

    setupNumberFormatting() {
        const numberInputs = document.querySelectorAll('input[type="number"]');
        numberInputs.forEach(input => {
            input.addEventListener('input', event => this.formatNumber(event.target));
        });

        const numberElements = document.querySelectorAll('[data-format="number"]');
        numberElements.forEach(el => {
            const value = parseFloat(el.textContent);
            if (!isNaN(value)) {
                el.textContent = this.formatCurrency(value);
            }
        });
    },

    formatNumber(input) {
        const value = parseFloat(input.value);
        if (!isNaN(value)) {
            input.setAttribute('data-formatted', this.formatCurrency(value));
        }
    },

    formatCurrency(amount) {
        return new Intl.NumberFormat('vi-VN', {
            style: 'decimal',
            minimumFractionDigits: 0,
            maximumFractionDigits: 0
        }).format(amount);
    },

    setupSearchFeatures() {
        const searchInputs = document.querySelectorAll('input[name="search"]');
        searchInputs.forEach(input => {
            let searchTimeout;

            input.addEventListener('input', event => {
                clearTimeout(searchTimeout);
                searchTimeout = setTimeout(() => {
                    this.handleSearch(event.target.value);
                }, this.config.debounceDelay);
            });

            this.setupSearchSuggestions(input);
        });
    },

    handleSearch(query) {
        if (query.length < 2) return;

        console.log('Searching for:', query);
        // Implement search logic here
        // This could trigger AJAX requests for suggestions
    },

    setupSearchSuggestions(input) {
        const suggestions = document.createElement('div');
        suggestions.className = 'search-suggestions position-absolute bg-white border rounded shadow-sm d-none';
        suggestions.style.zIndex = '1000';
        suggestions.style.width = '100%';
        suggestions.style.top = '100%';
        suggestions.style.left = '0';

        input.parentNode.style.position = 'relative';
        input.parentNode.appendChild(suggestions);

        document.addEventListener('click', event => {
            if (!input.parentNode.contains(event.target)) {
                suggestions.classList.add('d-none');
            }
        });
    },

    setupLoadingStates() {
        const submitButtons = document.querySelectorAll('button[type="submit"], .btn-submit');
        submitButtons.forEach(button => {
            button.addEventListener('click', () => {
                this.showButtonLoading(button);
            });
        });

        this.setupAjaxLoading();
    },

    showButtonLoading(button) {
        const originalText = button.innerHTML;
        button.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Đang xử lý...';
        button.disabled = true;
        button.setAttribute('data-original-text', originalText);

        setTimeout(() => {
            this.hideButtonLoading(button);
        }, 10000);
    },

    hideButtonLoading(button) {
        const originalText = button.getAttribute('data-original-text');
        if (originalText) {
            button.innerHTML = originalText;
            button.disabled = false;
            button.removeAttribute('data-original-text');
        }
    },

    showLoadingState(form) {
        const submitBtn = form.querySelector('button[type="submit"]');
        if (submitBtn) {
            this.showButtonLoading(submitBtn);
        }

        const loadingOverlay = document.getElementById('loadingOverlay');
        if (loadingOverlay) {
            loadingOverlay.classList.remove('d-none');
        }
    },

    setupAjaxLoading() {
        // Show loading overlay for AJAX requests
        const originalFetch = window.fetch;
        window.fetch = (...args) => {
            this.showGlobalLoading();
            return originalFetch(...args)
                .finally(() => this.hideGlobalLoading());
        };
    },

    showGlobalLoading() {
        const loadingOverlay = document.getElementById('loadingOverlay');
        if (loadingOverlay) {
            loadingOverlay.classList.remove('d-none');
        }
    },

    hideGlobalLoading() {
        const loadingOverlay = document.getElementById('loadingOverlay');
        if (loadingOverlay) {
            loadingOverlay.classList.add('d-none');
        }
    },

    setupSmoothScrolling() {
        const scrollLinks = document.querySelectorAll('a[href^="#"]');
        scrollLinks.forEach(link => {
            link.addEventListener('click', event => {
                event.preventDefault();
                const target = document.querySelector(link.getAttribute('href'));
                if (target) {
                    target.scrollIntoView({
                        behavior: 'smooth',
                        block: 'start'
                    });
                }
            });
        });
    },

    setupTooltips() {
        // Add tooltips to truncated text
        const truncatedElements = document.querySelectorAll('.text-truncate');
        truncatedElements.forEach(el => {
            if (el.scrollWidth > el.clientWidth) {
                el.setAttribute('title', el.textContent);
                el.setAttribute('data-bs-toggle', 'tooltip');
                new bootstrap.Tooltip(el);
            }
        });
    },

    setupDropdowns() {
        const dropdowns = document.querySelectorAll('.dropdown-toggle');
        dropdowns.forEach(dropdown => {
            dropdown.addEventListener('shown.bs.dropdown', () => {
                const firstLink = dropdown.nextElementSibling.querySelector('a, button');
                if (firstLink) firstLink.focus();
            });
        });
    },

    setupThemeToggle() {
        const themeToggle = document.getElementById('themeToggle');
        if (!themeToggle) return;

        const currentTheme = localStorage.getItem('theme') || 'light';
        document.documentElement.setAttribute('data-theme', currentTheme);
        this.updateThemeToggle(currentTheme);

        themeToggle.addEventListener('click', () => {
            const currentTheme = document.documentElement.getAttribute('data-theme');
            const newTheme = currentTheme === 'dark' ? 'light' : 'dark';

            document.documentElement.setAttribute('data-theme', newTheme);
            localStorage.setItem('theme', newTheme);
            this.updateThemeToggle(newTheme);

            this.showToast(`Đã chuyển sang chế độ ${newTheme === 'dark' ? 'tối' : 'sáng'}`, 'info');
        });
    },

    updateThemeToggle(theme) {
        const themeToggle = document.getElementById('themeToggle');
        if (!themeToggle) return;

        const icon = themeToggle.querySelector('i');
        if (theme === 'dark') {
            icon.className = 'fas fa-sun';
            themeToggle.setAttribute('title', 'Chuyển sang chế độ sáng');
        } else {
            icon.className = 'fas fa-moon';
            themeToggle.setAttribute('title', 'Chuyển sang chế độ tối');
        }
    },

    showToast(message, type = 'info') {
        const toastContainer = this.getOrCreateToastContainer();
        const toastId = 'toast-' + Date.now();

        const toastHTML = `
            <div id="${toastId}" class="toast align-items-center text-white bg-${this.getToastClass(type)} border-0" role="alert">
                <div class="d-flex">
                    <div class="toast-body">
                        <i class="${this.getToastIcon(type)} me-2"></i>
                        ${message}
                    </div>
                    <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
                </div>
            </div>
        `;

        toastContainer.insertAdjacentHTML('beforeend', toastHTML);

        const toastElement = document.getElementById(toastId);
        const toast = new bootstrap.Toast(toastElement);
        toast.show();

        toastElement.addEventListener('hidden.bs.toast', () => {
            toastElement.remove();
        });
    },

    getOrCreateToastContainer() {
        let container = document.querySelector('.toast-container');
        if (!container) {
            container = document.createElement('div');
            container.className = 'toast-container position-fixed top-0 end-0 p-3';
            container.style.zIndex = '1055';
            document.body.appendChild(container);
        }
        return container;
    },

    getToastClass(type) {
        const classes = {
            success: 'success',
            error: 'danger',
            warning: 'warning',
            info: 'info'
        };
        return classes[type] || 'info';
    },

    getToastIcon(type) {
        const icons = {
            success: 'fas fa-check-circle',
            error: 'fas fa-exclamation-circle',
            warning: 'fas fa-exclamation-triangle',
            info: 'fas fa-info-circle'
        };
        return icons[type] || 'fas fa-info-circle';
    },

    utils: {
        // Debounce function
        debounce(func, wait) {
            let timeout;
            return function executedFunction(...args) {
                const later = () => {
                    clearTimeout(timeout);
                    func(...args);
                };
                clearTimeout(timeout);
                timeout = setTimeout(later, wait);
            };
        },

        throttle(func, limit) {
            let inThrottle;
            return function() {
                const args = arguments;
                const context = this;
                if (!inThrottle) {
                    func.apply(context, args);
                    inThrottle = true;
                    setTimeout(() => inThrottle = false, limit);
                }
            };
        },

        copyToClipboard(text) {
            if (navigator.clipboard) {
                return navigator.clipboard.writeText(text);
            } else {
                // Fallback for older browsers
                const textArea = document.createElement('textarea');
                textArea.value = text;
                document.body.appendChild(textArea);
                textArea.select();
                document.execCommand('copy');
                document.body.removeChild(textArea);
                return Promise.resolve();
            }
        },

        generateId() {
            return Math.random().toString(36).substr(2, 9);
        },

        formatFileSize(bytes) {
            if (bytes === 0) return '0 Bytes';
            const k = 1024;
            const sizes = ['Bytes', 'KB', 'MB', 'GB'];
            const i = Math.floor(Math.log(bytes) / Math.log(k));
            return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
        },

        isValidEmail(email) {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            return emailRegex.test(email);
        },

        getUrlParameter(name) {
            const urlParams = new URLSearchParams(window.location.search);
            return urlParams.get(name);
        },

        setUrlParameter(name, value) {
            const url = new URL(window.location);
            url.searchParams.set(name, value);
            window.history.pushState({}, '', url);
        }
    }
};

function showToast(message, type = 'info') {
    BookShelfApp.showToast(message, type);
}

function formatCurrency(amount) {
    return BookShelfApp.formatCurrency(amount);
}

function copyToClipboard(text) {
    return BookShelfApp.utils.copyToClipboard(text)
        .then(() => showToast('Đã sao chép vào clipboard!', 'success'))
        .catch(() => showToast('Không thể sao chép', 'error'));
}

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', () => BookShelfApp.init());
} else {
    BookShelfApp.init();
}

document.addEventListener('DOMContentLoaded', function() {
    const quickAmountButtons = document.querySelectorAll('.quick-amount');
    const budgetAmountInput = document.getElementById('budgetAmount');

    if (budgetAmountInput) {
        quickAmountButtons.forEach(button => {
            button.addEventListener('click', function() {
                const amount = this.getAttribute('data-amount');
                budgetAmountInput.value = amount;

                quickAmountButtons.forEach(btn => {
                    btn.classList.remove('active');
                    btn.classList.add('btn-outline-primary');
                    btn.classList.remove('btn-primary');
                });

                this.classList.add('active');
                this.classList.remove('btn-outline-primary');
                this.classList.add('btn-primary');
            });
        });

        budgetAmountInput.addEventListener('input', function() {
            quickAmountButtons.forEach(btn => {
                btn.classList.remove('active');
                btn.classList.add('btn-outline-primary');
                btn.classList.remove('btn-primary');
            });
        });
    }
});

document.addEventListener('DOMContentLoaded', function() {
    const textareas = document.querySelectorAll('textarea[maxlength]');
    textareas.forEach(textarea => {
        const maxLength = textarea.getAttribute('maxlength');
        const counter = document.getElementById('charCount');

        if (counter) {
            function updateCounter() {
                const count = textarea.value.length;
                counter.textContent = count;

                if (count > maxLength * 0.9) {
                    counter.className = 'text-warning';
                } else if (count > maxLength * 0.95) {
                    counter.className = 'text-danger';
                } else {
                    counter.className = 'text-muted';
                }
            }

            textarea.addEventListener('input', updateCounter);
            updateCounter();
        }
    });
});

if (typeof module !== 'undefined' && module.exports) {
    module.exports = BookShelfApp;
}
