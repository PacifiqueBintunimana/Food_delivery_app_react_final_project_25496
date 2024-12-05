
/* eslint-disable no-unused-vars */

import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Homepage1 from './components/Homepage1';
import Login1 from './components/Login1';
import Register1 from './components/Register1';
import ForgotPassword from './components/forgotPassword';
import ResetPassword from './components/resetPassword';
import AdminDashboard from './components/AdminDashboard';
// import ProtectedRoute from './components/ProtectedRoute';
import AddUser from './components/AddUser';
import EditUser from './components/EditUser';
import SearchUser from './components/SearchUser';
import UserList from './components/UserList';
import 'bootstrap/dist/css/bootstrap.min.css';
import DownloadUsers from './components/DownloadUsers';
import UploadUsers from './components/UploadUsers';
import FileUpload from './components/FileUpload';
import CustomerDashboard from './components/CustomerDashboard';
import ManagerDashboard from './components/ManagerDashboard';
import { I18nextProvider } from 'react-i18next';
import './i18n';

function App() {
  return (
    <I18nextProvider>
    <Router future={{ 
      v7_startTransition: true,
      v7_relativeSplatPath: true 
    }}>
      <Routes>
        {/* Admin Route - Changed from /api/admin to /admin */}
        {/* <Route path="/admin" element={
          <ProtectedRoute>
            <AdminDashboard />
          </ProtectedRoute>
        } /> */}
        <Route path="/admin" element={<AdminDashboard />} />
        <Route path="/admin/add" element={<AddUser />} />
        <Route path="/admin/users/edit/:id" element={<EditUser />} />
        <Route path="/admin/search" element={<SearchUser />} />
        <Route path="/admin/users" element={<UserList />} />
        <Route path="/" element={<Homepage1 />} />
        <Route path="/login" element={<Login1 />} />
        <Route path="/register" element={<Register1 />} />
        <Route path="/customerDashboard" element={<CustomerDashboard/>} />
        <Route path="/managerDashboard" element={<ManagerDashboard/>} />
        <Route path="/forgotPassword" element={<ForgotPassword />} />
        <Route path="/resetPassword/:token" element={<ResetPassword />} />
        <Route path="/admin/download/users" element={<DownloadUsers />} />
        <Route path="/files/upload" element={<FileUpload />} />
        <Route path="/admin/upload/users" element={<UploadUsers />} />

      </Routes>
    </Router>
    </I18nextProvider>
  );
}


export default App;
