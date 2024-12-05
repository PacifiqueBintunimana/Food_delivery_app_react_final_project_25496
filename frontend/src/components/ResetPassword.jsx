/* eslint-disable react-hooks/rules-of-hooks */
/* eslint-disable no-unused-vars */
import React, { useState } from 'react';
import { Link, useParams, useNavigate } from 'react-router-dom';
// import axios from 'axios';
import { authService } from './auth';
const ResetPassword = () => {
  const { token } = useParams();
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    newPassword: '',
    confirmNewPassword: ''
  });
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.id]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (formData.newPassword !== formData.confirmNewPassword) {
      setError('Passwords do not match');
      return;
    }

    try {
      const response = await authService.resetPassword(token, formData.newPassword);
      setMessage('Password reset successful!');
      setTimeout(() => navigate('/login'), 2000);
  } catch (err) {
      setError(err.response?.data?.error || 'Unable to reset password. Please try again.');
  }
  
  };

  return (
    <section style={{
      fontFamily: 'Arial, sans-serif',
      margin: 0,
      padding: 0,
      background: 'linear-gradient(135deg, #6a11cb, #2575fc)',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      minHeight: '100vh',
      color: '#ffffff'
    }}>
      <div style={{
        width: '100%',
        maxWidth: '450px',
        background: '#ffffff',
        borderRadius: '10px',
        boxShadow: '0 8px 16px rgba(0, 0, 0, 0.2)',
        overflow: 'hidden',
        textAlign: 'center',
        padding: '2rem',
        color: '#333'
      }}>
        <div className="motivational-text">
          <h2 style={{ fontSize: '1.8rem', color: '#2575fc', marginBottom: '0.5rem' }}>
            Reset Your Password
          </h2>
          <p style={{ color: '#666', fontSize: '1rem', marginBottom: '1rem' }}>
            We understand that forgetting your password can be frustrating.
          </p>
          <p style={{ color: '#666', fontSize: '1rem', marginBottom: '1rem' }}>
            Enter your new password below to regain access to your account.
          </p>
        </div>

        {error && <div style={{ color: '#d9534f', background: '#f2dede', padding: '0.5rem', borderRadius: '5px', marginBottom: '1rem' }}>{error}</div>}
        {message && <div style={{ color: '#5cb85c', background: '#dff0d8', padding: '0.5rem', borderRadius: '5px', marginBottom: '1rem' }}>{message}</div>}

        <form onSubmit={handleSubmit}>
          <div style={{ marginBottom: '1rem', textAlign: 'left' }}>
            <label htmlFor="newPassword">New Password:</label>
            <input
              type="password"
              id="newPassword"
              value={formData.newPassword}
              onChange={handleChange}
              style={{
                width: '100%',
                padding: '0.7rem',
                marginTop: '0.5rem',
                border: '1px solid #ddd',
                borderRadius: '5px'
              }}
              required
            />
          </div>

          <div style={{ marginBottom: '1rem', textAlign: 'left' }}>
            <label htmlFor="confirmNewPassword">Confirm New Password:</label>
            <input
              type="password"
              id="confirmNewPassword"
              value={formData.confirmNewPassword}
              onChange={handleChange}
              style={{
                width: '100%',
                padding: '0.7rem',
                marginTop: '0.5rem',
                border: '1px solid #ddd',
                borderRadius: '5px'
              }}
              required
            />
          </div>

          <button type="submit" style={{
            width: '100%',
            padding: '0.7rem',
            backgroundColor: '#2575fc',
            color: '#ffffff',
            border: 'none',
            borderRadius: '5px',
            cursor: 'pointer',
            marginTop: '1rem'
          }}>
            Reset Password
          </button>
        </form>

        <p style={{ marginTop: '1rem' }}>
          <Link to="/login" style={{ color: '#2575fc', textDecoration: 'none' }}>
            Return to login
          </Link>
        </p>
      </div>
    </section>
  );
};

export default ResetPassword;
