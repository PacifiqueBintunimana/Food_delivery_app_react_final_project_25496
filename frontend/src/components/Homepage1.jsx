/* eslint-disable no-unused-vars */
import React, { useState } from "react";
import { Link } from "react-router-dom";
import headerImage from "../assets/assets/image/food-background.jpg";
import logoImage from "../assets/assets/image/food-background.jpg";
import TopMealsCarousel from "./TopMealsCarousel";

const Homepage1 = () => {
  const [language, setLanguage] = useState("en");

  const toggleLanguage = (lang) => {
    setLanguage(lang);
  };

  return (
    <div style={{ fontFamily: "Arial, sans-serif", margin: 0, minHeight: "100vh" }}>
      {/* Navbar */}
      <nav
        style={{
          display: "flex",
    flexWrap: "wrap",
    justifyContent: "space-between",
    alignItems: "center",
    padding: "1rem 2rem",
    background: "#ff5722",
    color: "#fff",
    boxShadow: "0px 4px 10px rgba(0, 0, 0, 0.2)",
        }}
      >
        <div className="nav__logo" style={{ display: "flex", alignItems: "center", gap: "10px" }}>
          <img src={logoImage} alt="Hub Logo" style={{ height: "50px",width:"50px" }} />
          <h2>FoodExPress</h2>
        </div>
        <ul
          style={{
            listStyle: "none",
            display: "flex",
            gap: "1.5rem",
          }}
        >
          <li>
            <Link to="/login" style={{ color: "#fff", textDecoration: "none" }}>
              Login
            </Link>
          </li>
          <li>
            <Link to="/register" style={{ color: "#fff", textDecoration: "none" }}>
              Register
            </Link>
          </li>
          <li>
            <a href="#" style={{ color: "#fff" }}>
              <i className="ri-twitter-fill"></i>
            </a>
          </li>
          <li>
            <a href="#" style={{ color: "#fff" }}>
              <i className="ri-facebook-circle-fill"></i>
            </a>
          </li>
          <li>
            <a href="#" style={{ color: "#fff" }}>
              <i className="ri-instagram-line"></i>
            </a>
          </li>
        </ul>
        <div className="language-switcher" style={{ display: "flex", gap: "10px" }}>
          <button
            onClick={() => toggleLanguage("en")}
            style={{
              backgroundColor: language === "en" ? "#ffa726" : "transparent",
              color: "#fff",
              border: "1px solid #fff",
              padding: "0.5rem 1rem",
              borderRadius: "5px",
              cursor: "pointer",
            }}
          >
            English
          </button>
          <button
            onClick={() => toggleLanguage("fr")}
            style={{
              backgroundColor: language === "fr" ? "#ffa726" : "transparent",
              color: "#fff",
              border: "1px solid #fff",
              padding: "0.5rem 1rem",
              borderRadius: "5px",
              cursor: "pointer",
            }}
          >
            French
          </button>
        </div>
      </nav>
<div>
      {/* Main Content */}
      <header style={{ padding: "4rem 2rem", textAlign: "center" }}>
        <img src={headerImage} alt="Header" style={{ width: "100%", borderRadius: "8px" }} />
        <h1>Welcome to FoodHub</h1>
        <p> Explore a wide range of delicious cuisines, track your orders in real-time, and enjoy
            personalized recommendations. Whether you’re craving comfort food or exploring new
            tastes, FoodHub is here to deliver happiness to your doorstep.</p>
      
           
      </header>
      <form style={{ marginTop: "2rem" }}>
            <div
              style={{
                display: "flex",
                gap: "1rem",
                justifyContent: "center",
                marginBottom: "1rem",
              }}
            >
              <div
                style={{
                  display: "flex",
                  alignItems: "center",
                  background: "#fff",
                  borderRadius: "5px",
                  padding: "0.5rem 1rem",
                }}
              >
                <span>
                  <i className="ri-search-line"></i>
                </span>
                <input
                  type="text"
                  placeholder="Search for restaurant"
                  style={{
                    border: "none",
                    outline: "none",
                    paddingLeft: "10px",
                  }}
                />
              </div>
              <div
                style={{
                  display: "flex",
                  alignItems: "center",
                  background: "#fff",
                  borderRadius: "5px",
                  padding: "0.5rem 1rem",
                }}
              >
                <span>
                  <i className="ri-arrow-down-s-line"></i>
                </span>
                <input
                  type="text"
                  placeholder="Menu - Food Category"
                  style={{
                    border: "none",
                    outline: "none",
                    paddingLeft: "10px",
                  }}
                />
              </div>
            </div>
            <button 
              type="submit"
              style={{
                backgroundColor: "#ff5722",
                color: "#fff",
                padding: "0.7rem 1.5rem",
                border: "none",
                borderRadius: "5px",
                cursor: "pointer",
              }}
            >
              Search Now
            </button>
          </form>
      </div>

      
      {/* TopMealsCarousel */}
      <div>
        <TopMealsCarousel />
      </div>

      {/* Footer */}
      <footer
        style={{
          background: "#333",
          color: "#fff",
          padding: "2rem 1rem",
          display: "flex",
          flexWrap: "wrap",
          justifyContent: "space-between",
          alignItems: "center",
          gap: "1rem",
        }}
      >
        {/* Contact Info */}
        <div style={{ flex: "1 1 30%" }}>
          <h3>Contact Us</h3>
          <p>
            <i className="ri-mail-line" style={{ marginRight: "8px" }}></i>
            <a
              href="mailto:bintunimana.pacifique@gmail.com"
              style={{ color: "#ffa726", textDecoration: "none" }}
            >
              bintunimana.pacifique@gmail.com
            </a>
          </p>
          <p>
            <i className="ri-phone-line" style={{ marginRight: "8px" }}></i>
            <a href="tel:+25085363827" style={{ color: "#ffa726", textDecoration: "none" }}>
              +250 853 63827
            </a>
          </p>
        </div>

        {/* Social Media Links */}
        <div style={{ flex: "1 1 30%", textAlign: "center" }}>
          <h3>Follow Us</h3>
          <div style={{ display: "flex", justifyContent: "center", gap: "1rem" }}>
            <a href="#" style={{ color: "#fff", fontSize: "1.5rem" }}>
              <i className="ri-facebook-circle-fill"></i>
            </a>
            <a href="#" style={{ color: "#fff", fontSize: "1.5rem" }}>
              <i className="ri-twitter-fill"></i>
            </a>
            <a href="#" style={{ color: "#fff", fontSize: "1.5rem" }}>
              <i className="ri-instagram-line"></i>
            </a>
          </div>
        </div>

        {/* Newsletter Subscription */}
        <div style={{ flex: "1 1 30%" }}>
          <h3>Stay Updated</h3>
          <form style={{ display: "flex", flexDirection: "column", gap: "0.5rem" }}>
            <input
              type="email"
              placeholder="Enter your email"
              style={{
                padding: "0.5rem",
                borderRadius: "5px",
                border: "1px solid #fff",
                outline: "none",
              }}
            />
            <button
              type="submit"
              style={{
                backgroundColor: "#ffa726",
                color: "#fff",
                border: "none",
                padding: "0.5rem 1rem",
                borderRadius: "5px",
                cursor: "pointer",
              }}
            >
              Subscribe
            </button>
          </form>
        </div>
      </footer>
    </div>
  );
};

export default Homepage1;
