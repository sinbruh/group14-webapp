"use client"

import React, { useState, useEffect } from 'react';
import { asyncApiRequest } from '@/services/request';
import { useStore, isLoggedIn } from '@/services/authentication';
import Navbar from "@/components/Navbar";
import { Card, CardContent } from "@/components/ui/card";
import { Car, MapPin, CalendarDays } from "lucide-react";

export default function Page() {
    const user = useStore((state) => state.user);
    const [userData, setUserData] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        async function fetchUserData() {
            if (user) {
                try {
                    setLoading(true);
                    const data = await asyncApiRequest('GET', `/api/users/${user.email}`);
                    if (data) {
                        setUserData(data);
                    }
                    setLoading(false);
                } catch (error) {
                    console.error("Error fetching user data:", error);
                    setLoading(false);
                }
            }
        }

        fetchUserData();
    }, [user]);

    function formatDate(timestamp) {
        return new Date(timestamp).toLocaleDateString();
    }

    if (!isLoggedIn()) {
        return (
            <div className="flex flex-col min-h-screen">
                <Navbar />
                <main className="flex-1 bg-gradient-to-b from-emerald-50 to-white py-12 md:py-24 lg:py-32">
                    <div className="container px-4 md:px-6 mx-auto">
                        <div className="text-center">
                            <h1 className="text-3xl font-bold tracking-tighter sm:text-5xl">Please log in to view your profile</h1>
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
                            <h1 className="text-3xl font-bold tracking-tighter sm:text-5xl">My Profile</h1>
                            <p className="max-w-[600px] text-gray-500 md:text-xl mx-auto">
                                View your order history and current rentals
                            </p>
                        </div>

                        {loading ? (
                            <div className="text-center py-12">
                                <p className="text-gray-500">Loading your profile data...</p>
                            </div>
                        ) : (
                            <div className="grid gap-8 md:grid-cols-1 lg:grid-cols-2">
                                <Card className="shadow-lg">
                                    <CardContent className="p-6">
                                        <div className="flex items-center gap-4 mb-6">
                                            <div className="flex h-12 w-12 items-center justify-center rounded-full bg-emerald-100">
                                                <CalendarDays className="h-6 w-6 text-emerald-600" />
                                            </div>
                                            <h2 className="text-2xl font-semibold">Order History</h2>
                                        </div>

                                        {!userData?.receipts || userData.receipts.length === 0 ? (
                                            <div className="text-center py-8 bg-gray-50 rounded-lg">
                                                <p className="text-gray-500">You don't have any orders yet.</p>
                                            </div>
                                        ) : (
                                            <div className="space-y-6">
                                                {userData.receipts.map((receipt) => (
                                                    <div key={receipt.id} className="border rounded-lg p-4 shadow-sm hover:shadow-md transition-shadow">
                                                        <div className="flex justify-between items-center mb-2">
                                                            <h3 className="font-medium text-lg">{receipt.carName}</h3>
                                                            <span className="text-emerald-600 font-medium">${receipt.totalPrice}</span>
                                                        </div>
                                                        <div className="text-sm text-gray-600">
                                                            <div className="flex items-center gap-2 mb-1">
                                                                <Car className="h-4 w-4 text-emerald-500" />
                                                                <p>Provider: {receipt.providerName}</p>
                                                            </div>
                                                            <div className="flex items-center gap-2 mb-1">
                                                                <MapPin className="h-4 w-4 text-emerald-500" />
                                                                <p>Location: {receipt.location}</p>
                                                            </div>
                                                            <div className="flex items-center gap-2">
                                                                <CalendarDays className="h-4 w-4 text-emerald-500" />
                                                                <p>
                                                                    Rental Period: {formatDate(receipt.startDate)} - {formatDate(receipt.endDate)}
                                                                </p>
                                                            </div>
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
                                            <h2 className="text-2xl font-semibold">Current Rentals</h2>
                                        </div>

                                        {!userData?.rentals || userData.rentals.length === 0 ? (
                                            <div className="text-center py-8 bg-gray-50 rounded-lg">
                                                <p className="text-gray-500">You don't have any active rentals.</p>
                                            </div>
                                        ) : (
                                            <div className="space-y-6">
                                                {userData.rentals.map((rental) => (
                                                    <div key={rental.id} className="border rounded-lg p-4 shadow-sm hover:shadow-md transition-shadow">
                                                        <div className="flex justify-between items-center mb-2">
                                                            <h3 className="font-medium text-lg">
                                                                {rental.provider?.configuration?.car?.make} {rental.provider?.configuration?.car?.model}
                                                            </h3>
                                                        </div>
                                                        <div className="text-sm text-gray-600">
                                                            <div className="flex items-center gap-2 mb-1">
                                                                <Car className="h-4 w-4 text-emerald-500" />
                                                                <p>Provider: {rental.provider?.name}</p>
                                                            </div>
                                                            <div className="flex items-center gap-2 mb-1">
                                                                <MapPin className="h-4 w-4 text-emerald-500" />
                                                                <p>Location: {rental.provider?.location}</p>
                                                            </div>
                                                            <div className="flex items-center gap-2">
                                                                <CalendarDays className="h-4 w-4 text-emerald-500" />
                                                                <p>
                                                                    Rental Period: {formatDate(rental.startDate)} - {formatDate(rental.endDate)}
                                                                </p>
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
