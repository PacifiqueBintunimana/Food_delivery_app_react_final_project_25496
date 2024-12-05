import api from './api.js';


export const checkAuth = () => {
  const token = localStorage.getItem('token');
  const role = localStorage.getItem('role');
  return token && role === 'ROLE_ADMIN';
};

export const authService = {
    login: async (credentials) => {
        const response = await api.post('/login', credentials);
        if (response.data) {
            localStorage.setItem('token', 'dummy-token');
            localStorage.setItem('role', response.data);
        }
        return response;
    },
    
    register: (userData) => api.post('/register', userData),
    
    logout: () => {
        localStorage.removeItem('token');
        localStorage.removeItem('role');
        return api.post('/logout');
    },
    
    forgotPassword: (email) => api.post('/api/auth/forgotPassword', null, {
        params: {
            email: email
        },
        timeout: 15000
    }),
    
    resetPassword: (token, newPassword) => 
        api.post(`/api/auth/resetPassword/${token}`, {
            newPassword: newPassword,
            confirmNewPassword: newPassword
        }),
    
    getNotifications: () => api.get('/notifications/user/unread'),
    
    markNotificationsAsRead: () => 
        api.put('/notifications/user/mark-all-as-read'),

    // New admin-specific methods
    getUsers: (page, size) => 
        api.get('/api/admin/users', {
            params: { page, size }
        }),

    deleteUser: (userId) => 
        api.delete(`/api/admin/users/${userId}`),

    updateUser: (userId, userData) => 
        api.put(`/api/admin/users/${userId}`, userData),

    addUser: (userData) => 
        api.post('/api/admin/users', userData),

    userUpload: (userData) => 
        api.post('/api/admin/upload/users', userData),
    FileUpload: (userData) => 
        api.post('/api/files/upload', userData),

    downloadUsers: (userData) => 
        api.get('/api/admin/download/users', userData),

    searchUsers: (query) => 
        api.get('/api/admin/search/results', {
            params: { query }
        })
};

export default authService;
