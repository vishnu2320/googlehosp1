```
import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api', // Backend URL
    headers: {
        'Content-Type': 'application/json',
    },
});

// Lab Management
api.getLabTests = (hospitalId) => api.get(`/ hospitals / ${ hospitalId }/lab-tests`);
api.createLabTest = (data) => api.post('/lab-tests', data);
api.requestTest = (data) => api.post('/test-requests', data);
api.getPendingTestRequests = (hospitalId) => api.get(`/hospitals/${hospitalId}/test-requests/pending`);
api.updateTestResult = (id, data) => api.put(`/test-requests/${id}/result`, data);
api.getPatientTestRequests = (patientId) => api.get(`/patients/${patientId}/test-requests`);

// Add token to requests if available
api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Handle 401 unauthorized responses
api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response?.status === 401) {
            // Token expired or invalid
            localStorage.removeItem('token');
            localStorage.removeItem('user');
            window.location.href = '/login';
        }
        return Promise.reject(error);
    }
);

export default api;
