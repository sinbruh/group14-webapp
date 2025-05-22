"use client"

import React, { useState, useEffect } from 'react';
import { asyncApiRequest } from '@/services/request';
import { isAdmin, isLoggedIn } from '@/services/authentication';
import Navbar from "@/components/Navbar";
import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Users, Car, Settings, Trash2, Edit, EyeOff } from "lucide-react";
import {flattenCars} from "@/lib/utils";

export default function Page() {
    const [users, setUsers] = useState([]);
    const [cars, setCars] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (isAdmin()) {
            fetchData();
        } else {
            setLoading(false);
        }
    }, []);

    async function fetchData() {
        setLoading(true);
        try {
            await Promise.all([fetchUsers(), fetchCars()]);
        } finally {
            setLoading(false);
        }
    }

    async function fetchUsers() {
        try {
            const data = await asyncApiRequest('GET', '/api/users');
            setUsers(data);
        } catch (error) {
            console.error("Error fetching users:", error);
        }
    }

    async function fetchCars() {
        try {
            const data = await asyncApiRequest('GET', '/api/cars');
            setCars(flattenCars(data));
        } catch (error) {
            console.error("Error fetching cars:", error);
        }
    }

    async function handleDeleteUser(userId) {
        try {
            await asyncApiRequest('DELETE', `/api/users/${userId}`);
            await fetchUsers();
        } catch (error) {
            console.error("Error deleting user:", error);
        }
    }

    async function handleDeleteCar(carId) {
        try {
            await asyncApiRequest('DELETE', `/api/cars/${carId}`);
            await fetchCars();
        } catch (error) {
            console.error("Error deleting car:", error);
        }
    }

    if (!isLoggedIn()) {
        return (
            <div className="flex flex-col min-h-screen">
                <Navbar />
                <main className="flex-1 bg-gradient-to-b from-emerald-50 to-white py-12 md:py-24 lg:py-32">
                    <div className="container px-4 md:px-6 mx-auto">
                        <div className="text-center">
                            <h1 className="text-3xl font-bold tracking-tighter sm:text-5xl">Please log in to access the admin dashboard</h1>
                        </div>
                    </div>
                </main>
            </div>
        );
    }

    if (!isAdmin()) {
        return (
            <div className="flex flex-col min-h-screen">
                <Navbar />
                <main className="flex-1 bg-gradient-to-b from-emerald-50 to-white py-12 md:py-24 lg:py-32">
                    <div className="container px-4 md:px-6 mx-auto">
                        <div className="text-center">
                            <h1 className="text-3xl font-bold tracking-tighter sm:text-5xl">Access Denied</h1>
                            <p className="max-w-[600px] text-gray-500 md:text-xl mx-auto mt-4">
                                You do not have permission to view this page. Admin privileges are required.
                            </p>
                        </div>
                    </div>
                </main>
            </div>
        );
    }

    return (
        <div className="flex flex-col min-h-screen">
            <Navbar />
            <main className="flex-1">
                <section className="w-full py-12 md:py-24 lg:py-32 bg-gradient-to-b from-emerald-50 to-white">
                    <div className="container px-4 md:px-6">
                        <div className="space-y-2 text-center mb-8">
                            <h1 className="text-3xl font-bold tracking-tighter sm:text-5xl">Admin Dashboard</h1>
                            <p className="max-w-[600px] text-gray-500 md:text-xl mx-auto">
                                Manage users and car configurations
                            </p>
                        </div>

                        {loading ? (
                            <div className="text-center py-12">
                                <p className="text-gray-500">Loading admin data...</p>
                            </div>
                        ) : (
                            <div className="grid gap-8 md:grid-cols-1 lg:grid-cols-2">
                                <Card className="shadow-lg">
                                    <CardContent className="p-6">
                                        <div className="flex items-center gap-4 mb-6">
                                            <div className="flex h-12 w-12 items-center justify-center rounded-full bg-emerald-100">
                                                <Users className="h-6 w-6 text-emerald-600" />
                                            </div>
                                            <h2 className="text-2xl font-semibold">Users</h2>
                                        </div>

                                        {users.length === 0 ? (
                                            <div className="text-center py-8 bg-gray-50 rounded-lg">
                                                <p className="text-gray-500">No users found.</p>
                                            </div>
                                        ) : (
                                            <div className="space-y-4">
                                                {users.map(user => (
                                                    <div key={user.id} className="border rounded-lg p-4 shadow-sm hover:shadow-md transition-shadow">
                                                        <div className="flex justify-between items-center">
                                                            <div>
                                                                <h3 className="font-medium text-lg">{user.email}</h3>
                                                                {user.roles && (
                                                                    <div className="text-sm text-gray-500 mt-1">
                                                                        Roles: {user.roles.map(role => {
                                                                        switch (role.name) {
                                                                            case 'ROLE_ADMIN':
                                                                                return 'Administrator';
                                                                            case 'ROLE_USER':
                                                                                return 'User';
                                                                            default:
                                                                                return role.name;
                                                                        }
                                                                    }).join(', ')}
                                                                    </div>
                                                                )}
                                                            </div>
                                                            <Button 
                                                                variant="destructive" 
                                                                size="sm"
                                                                onClick={() => handleDeleteUser(user.email)}
                                                                className="bg-red-500 hover:bg-red-600"
                                                            >
                                                                <Trash2 className="h-4 w-4 mr-1" />
                                                                Delete
                                                            </Button>
                                                        </div>
                                                    </div>
                                                ))}
                                            </div>
                                        )}
                                    </CardContent>
                                </Card>

                                <Card className="shadow-lg">
                                    <CardContent className="p-6">
                                        <div className="flex items-center gap-4 mb-6">
                                            <div className="flex h-12 w-12 items-center justify-center rounded-full bg-emerald-100">
                                                <Car className="h-6 w-6 text-emerald-600" />
                                            </div>
                                            <h2 className="text-2xl font-semibold">Cars</h2>
                                        </div>

                                        {cars.length === 0 ? (
                                            <div className="text-center py-8 bg-gray-50 rounded-lg">
                                                <p className="text-gray-500">No cars found.</p>
                                            </div>
                                        ) : (
                                            <div className="space-y-4">
                                                {cars.map(car => (
                                                    <div key={car.id} className="border rounded-lg p-4 shadow-sm hover:shadow-md transition-shadow">
                                                        <div className="flex justify-between items-center">
                                                            <div>
                                                                <h3 className="font-medium text-lg">{car.make} {car.model}</h3>
                                                            </div>
                                                            <div className="flex gap-2">
                                                                <Button variant="outline" size="sm" className="border-emerald-500 text-emerald-500 hover:bg-emerald-50">
                                                                    <Edit className="h-4 w-4 mr-1" />
                                                                    Edit
                                                                </Button>
                                                                <Button variant="outline" size="sm" className="border-amber-500 text-amber-500 hover:bg-amber-50">
                                                                    <EyeOff className="h-4 w-4 mr-1" />
                                                                    Hide
                                                                </Button>
                                                                <Button 
                                                                    variant="destructive" 
                                                                    size="sm"
                                                                    onClick={() => handleDeleteCar(car.id)}
                                                                    className="bg-red-500 hover:bg-red-600"
                                                                >
                                                                    <Trash2 className="h-4 w-4 mr-1" />
                                                                    Delete
                                                                </Button>
                                                            </div>
                                                        </div>
                                                    </div>
                                                ))}
                                            </div>
                                        )}
                                    </CardContent>
                                </Card>
                            </div>
                        )}
                    </div>
                </section>
            </main>
            <footer className="flex flex-col gap-2 sm:flex-row py-6 w-full shrink-0 items-center px-4 md:px-6 border-t">
                <p className="text-xs text-gray-500">
                    © 2023 RentalRoulette. All rights reserved.
                </p>
            </footer>
        </div>
    );
}
