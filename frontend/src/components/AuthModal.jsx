"use client";

import { useState } from "react";

export default function AuthModal() {
    const [isLoginOpen, setIsLoginOpen] = useState(false);
    const [isSignupOpen, setIsSignupOpen] = useState(false);

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch("/api/authenticate", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    email: e.target.email.value,
                    password: e.target.email.value
                }),
            });

            if (response.ok) {
                const data = await response.json();
                alert("Login successful");
                localStorage.setItem("token", data.token);
            } else {
                const error = await response.text();
                alert("Login failed: " + error);
            }
        } catch (error) {
            console.error("Error during login:", error);
            alert("Login failed");
        }
    };

    const handleSignup = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch("/api/register", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    firstName: e.target.firstName.value,
                    lastName: e.target.lastName.value,
                    email: e.target.email.value,
                    phoneNumber: e.target.phoneNumber.value,
                    password: e.target.password.value,
                    dateOfBirth: e.target.dateOfBirth.value
                }),
            });

            if (response.ok) {
                const data = await response.json();
                alert("Signup successful");
                localStorage.setItem("token", data.token);
            } else {
                const error = await response.text();
                alert("Signup failed: " + error);
            }
        } catch (error) {
            console.error("Error during signup:", error);
            alert("Signup failed");
        }
    };

    return (
        <div>
            <button
                onClick={() => setIsLoginOpen(true)}
                className="bg-gradient-to-r from-emerald-500 to-emerald-700 text-white px-4 py-2 rounded-md shadow-sm hover:from-emerald-600 hover:to-emerald-800 text-sm transition-all duration-200"
            >
                Log In
            </button>
            <button
                onClick={() => setIsSignupOpen(true)}
                className="bg-gradient-to-r from-blue-500 to-blue-700 text-white px-4 py-2 rounded-md shadow-sm hover:from-blue-600 hover:to-blue-800 text-sm transition-all duration-200 ml-2"
            >
                Sign Up
            </button>
            {isLoginOpen && (
                <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 backdrop-blur-sm z-50">
                    <div className="bg-white p-6 rounded-lg shadow-lg w-full max-w-md mx-4">
                        <div className="flex justify-between items-center mb-4">
                            <h2 className="text-xl font-bold">Log In</h2>
                            <button
                                onClick={() => setIsLoginOpen(false)}
                                className="text-gray-500 hover:text-gray-700"
                            >
                                ✕
                            </button>
                        </div>
                        <form onSubmit={handleLogin} className="space-y-4">
                            <input
                                type="email"
                                placeholder="Email"
                                className="w-full border rounded-md p-2 focus:ring-2 focus:ring-emerald-500"
                            />
                            <input
                                type="password"
                                placeholder="Password"
                                className="w-full border rounded-md p-2 focus:ring-2 focus:ring-emerald-500"
                            />
                            <button
                                type="submit"
                                className="bg-emerald-600 text-white px-4 py-2 rounded-md w-full hover:bg-emerald-700"
                            >
                                Log In
                            </button>
                        </form>
                    </div>
                </div>
            )}
            {isSignupOpen && (
                <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 backdrop-blur-sm z-50">
                    <div className="bg-white p-6 rounded-lg shadow-lg w-full max-w-md mx-4">
                        <div className="flex justify-between items-center mb-4">
                            <h2 className="text-xl font-bold">Sign Up</h2>
                            <button
                                onClick={() => setIsSignupOpen(false)}
                                className="text-gray-500 hover:text-gray-700"
                            >
                                ✕
                            </button>
                        </div>
                        <form onSubmit={handleSignup} className="space-y-4">
                            <input
                                type="text"
                                placeholder="First Name"
                                className="w-full border rounded-md p-2 focus:ring-2 focus:ring-blue-500"
                            />
                            <input
                                type="text"
                                placeholder="Last Name"
                                className="w-full border rounded-md p-2 focus:ring-2 focus:ring-blue-500"
                            />
                            <input
                                type="email"
                                placeholder="Email"
                                className="w-full border rounded-md p-2 focus:ring-2 focus:ring-blue-500"
                            />
                            <input
                                type="tel"
                                placeholder="Phone Number"
                                className="w-full border rounded-md p-2 focus:ring-2 focus:ring-blue-500"
                            />
                            <input
                                type="password"
                                placeholder="Password"
                                className="w-full border rounded-md p-2 focus:ring-2 focus:ring-blue-500"
                            />
                            <input
                                type="date"
                                placeholder="Date of Birth"
                                className="w-full border rounded-md p-2 focus:ring-2 focus:ring-blue-500"
                            />
                            <button
                                type="submit"
                                className="bg-blue-600 text-white px-4 py-2 rounded-md w-full hover:bg-blue-700"
                            >
                                Sign Up
                            </button>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
}