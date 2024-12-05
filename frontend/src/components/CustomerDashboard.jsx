/* eslint-disable no-unused-vars */
import React, { useEffect, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';

const CustomerDashboard = () => {
  const [notifications, setNotifications] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetchUserNotifications();
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      const dashboardData = await axios.get('/api/customerDashboard');
      // Handle dashboard data
    } catch (error) {
      console.error('Error fetching dashboard data:', error);
    }
  };

  const fetchUserNotifications = async () => {
    try {
      const response = await axios.get('/api/notifications/user/unread');
      if (Array.isArray(response.data)) {
        setNotifications(response.data);
        await markAllNotificationsAsRead();
      } else {
        setNotifications([]);
      }
    } catch (error) {
      console.log('Error fetching notifications:', error);
      setNotifications([]);
    }
  };

  const markAllNotificationsAsRead = async () => {
    try {
      await axios.put('/api/notifications/user/mark-all-as-read');
    } catch (error) {
      console.log('Error marking notifications as read:', error);
    }
  };
  const handleLogout = () => {
    localStorage.removeItem('token');
    sessionStorage.clear();
    navigate('/login');
  };

  const dashboardStyle = {
    background: 'linear-gradient(135deg, #001f3f, #0074d9)',
    minHeight: '100vh',
    display: 'flex',
    justifyContent: 'center',
    alignItems: 'center',
    animation: 'fadeIn 1s ease'
  };

  const contentStyle = {
    backgroundColor: '#003366',
    padding: '2rem',
    maxWidth: '800px',
    width: '100%',
    borderRadius: '10px',
    boxShadow: '0 10px 20px rgba(0, 0, 0, 0.3)',
    textAlign: 'center',
    color: '#e0f7fa'
  };

  return (
    <div style={dashboardStyle}>
      <div style={contentStyle}>
        <h1>Customer Dashboard .Welcome to Food Delivery!</h1>
        <p className="welcome-message">
          Your personal hub for managing orders, tracking your orders.
        </p>

        <div className="order-summary">
          <h2>your orders available</h2>
          <p>Manage and track your food orders with ease</p>
          
          <Link to="/orders/current" className="btn">Current Orders</Link>
          <Link to="/orders/history" className="btn">Order History</Link>
        </div>

        <div className="restaurant-section">
          <h2>Explore Restaurants</h2>
          <p>Discover new cuisines and your favorite local eateries.</p>
          <Link to="/restaurants" className="btn">Browse Restaurants</Link>
          <Link to="/restaurants/favorites" className="btn">Favorite Restaurants</Link>
        </div>

        <div className="profile-management">
        <h2>Profile & Settings</h2>
          <p>Update your profile, payment methods, and delivery preferences.</p>
          <Link to="/profile" className="btn">Edit Profile</Link>
          <Link to="/addresses" className="btn">Manage Addresses</Link>
        </div>

        <div id="user-notifications">
          <h3>Notifications <span>{notifications.length}</span></h3>
          <div>
            {notifications && notifications.length > 0 ? (
              notifications.map((notification, index) => (
                <div key={index} className="notification-item">
                  <strong>{notification.title}</strong>
                  <p>{notification.message}</p>
                </div>
              ))
            ) : (
              <p>No new notifications</p>
            )}
          </div>
        </div>

        <button onClick={handleLogout}>
          Logout
        </button>
      </div>

      <style>
      {`
          @keyframes fadeIn {
            from { opacity: 0; }
            to { opacity: 1; }
          }

          @keyframes slideIn {
            from { transform: translateX(-20px); opacity: 0; }
            to { transform: translateX(0); opacity: 1; }
          }

          .btn {
            display: inline-block;
            margin: 0.5rem;
            padding: 10px 20px;
            background-color: #ff4500;
            color: #fff;
            text-decoration: none;
            border-radius: 5px;
            transition: background-color 0.3s, transform 0.3s;
          }

          .btn:hover {
            background-color: #ff6347;
            transform: scale(1.05);
          }

          .logout-btn {
            background-color: #dc143c;
          }

          .logout-btn:hover {
            background-color: #b22222;
          }

          .notification-item {
            padding: 0.5rem 0;
            border-bottom: 1px solid #ffa07a;
            animation: slideIn 0.5s ease forwards;
          }
        `}
      </style>
    </div>
  );
};

export default CustomerDashboard;
