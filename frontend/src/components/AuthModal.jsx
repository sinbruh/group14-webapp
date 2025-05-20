import { useState } from "react";
import { isAdmin, isUser, sendAuthenticationRequest, useStore } from "@/services/authentication";
import { useRouter } from "next/navigation";
import { asyncApiRequest } from "@/services/request";

export default function AuthModal() {
    const [isLoginOpen, setIsLoginOpen] = useState(false);
    const [isSignupOpen, setIsSignupOpen] = useState(false);
    const router = useRouter();
    const setUser = useStore((state) => state.setUser);

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            await sendAuthenticationRequest(
                e.target.email.value,
                e.target.password.value,
                (userData) => {
                    // Store user data in Zustand store
                    setUser(userData);

                    if (isUser()) {
                        setIsLoginOpen(false);
                    }
                },
                (error) => {
                    alert("Login failed: " + error);
                }
            );
        } catch (err) {
            console.error(`Error during login: ${err}`);
        }
    };

    const handleSignup = async (e) => {
        e.preventDefault();
        try {
            const requestBody = {
                firstName: e.target.firstName.value,
                lastName: e.target.lastName.value,
                email: e.target.email.value,
                phoneNumber: e.target.phoneNumber.value,
                password: e.target.password.value,
                dateOfBirth: Math.floor(new Date(e.target.dateOfBirth.value).getTime() / 1000)
            };

            const data = await asyncApiRequest("POST", "/api/register", requestBody);
            alert("Signup successful");
            localStorage.setItem("token", data.token);
            setIsSignupOpen(false);
        } catch (error) {
            console.error("Error during signup:", error);
            alert("Signup failed: " + error.message);
        }
    };

    return (
        <div>
            <button
                onClick={() => setIsLoginOpen(true)}
                className="bg-emerald-600 text-white px-4 py-2 rounded-md shadow-sm hover:bg-emerald-700 text-sm transition-all duration-200"
            >
                Log In
            </button>
            <button
                onClick={() => setIsSignupOpen(true)}
                className="bg-blue-600 text-white px-4 py-2 rounded-md shadow-sm hover:bg-blue-700 text-sm transition-all duration-200 ml-2"
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
                                name="email"
                                placeholder="Email"
                                className="w-full border rounded-md p-2 focus:ring-2 focus:ring-emerald-500"
                            />
                            <input
                                type="password"
                                name="password"
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
                                name="firstName"
                                placeholder="First Name"
                                className="w-full border rounded-md p-2 focus:ring-2 focus:ring-blue-500"
                            />
                            <input
                                type="text"
                                name="lastName"
                                placeholder="Last Name"
                                className="w-full border rounded-md p-2 focus:ring-2 focus:ring-blue-500"
                            />
                            <input
                                type="email"
                                name="email"
                                placeholder="Email"
                                className="w-full border rounded-md p-2 focus:ring-2 focus:ring-blue-500"
                            />
                            <input
                                type="tel"
                                name="phoneNumber"
                                placeholder="Phone Number"
                                className="w-full border rounded-md p-2 focus:ring-2 focus:ring-blue-500"
                            />
                            <input
                                type="password"
                                name="password"
                                placeholder="Password"
                                className="w-full border rounded-md p-2 focus:ring-2 focus:ring-blue-500"
                            />
                            <input
                                type="date"
                                name="dateOfBirth"
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
