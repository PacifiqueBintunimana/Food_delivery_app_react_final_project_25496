/* eslint-disable react-hooks/rules-of-hooks */
/* eslint-disable no-unused-vars */
import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import { authService } from './auth';

const ForgotPassword = () => {
  const [email, setEmail] = useState('');
  const [message, setMessage] = useState('');
  

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
        const response = await authService.forgotPassword(email);
        setMessage('Success! Reset link has been sent to your email. Please check your inbox.');
        setEmail('');
    } catch (error) {
        console.log('Request data:', { email });
        
        if (error.response) {
            setMessage(error.response.data.error || 'Server error. Please try again.');
        } else if (error.request) {
            setMessage('Network error. Please check your connection.');
        } else {
            setMessage('An unexpected error occurred. Please try again.');
        }
    }
};

  

  return (
    <div style={{
      fontFamily: 'Arial, sans-serif',
      backgroundColor: '#f9f9f9',
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      justifyContent: 'center',
      height: '100vh',
      margin: 0
    }}>
      <h1>Forgot Password</h1>
      {message && <div style={{ margin: '10px 0' }}>{message}</div>}
      
      <form onSubmit={handleSubmit} style={{
        backgroundColor: '#fff',
        padding: '20px',
        borderRadius: '5px',
        boxShadow: '0 0 10px rgba(0, 0, 0, 0.1)'
      }}>
        <div style={{ marginBottom: '15px' }}>
          <label htmlFor="email">Email:</label>
          <input
            type="email"
            id="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            style={{
              marginBottom: '10px',
              padding: '10px',
              border: '1px solid #ccc',
              borderRadius: '3px',
              width: '200px'
            }}
          />
        </div>
        
        <button type="submit" style={{
          padding: '10px',
          backgroundColor: '#007BFF',
          color: 'white',
          border: 'none',
          borderRadius: '3px',
          cursor: 'pointer',
          width: '100%'
        }}>
          Send Reset Link
        </button>
      </form>

      <p style={{ marginTop: '15px' }}>
        <Link to="/login" style={{
          color: '#007BFF',
          textDecoration: 'none'
        }}>
          Remembered your password? Log in
        </Link>
      </p>
    </div>
  );
};

export default ForgotPassword;
