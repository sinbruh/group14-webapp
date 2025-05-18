import React, { useEffect, useState } from "react";
import Link from "next/link";
import Image from "next/image";
import { asyncApiRequest } from "@/services/request";
import { getFavorites } from "@/services/favorite";
import {
    Bell,
    Calendar,
    Car,
    Edit,
    Heart,
    History,
    Settings,
} from "lucide-react";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Progress } from "@/components/ui/progress";

export default function UserProfilePage() {
    const [user, setUser] = useState(null);
    const [activeBookings, setActiveBookings] = useState([]);
    const [bookingHistory, setBookingHistory] = useState([]);
    const [favoriteVehicles, setFavoriteVehicles] = useState([]);
    const [loading, setLoading] = useState(true);

    //Todo: possibly add the active bookings and booking history to the backend.
    useEffect(() => {
        fetchData();
    }, []);

    async function fetchData() {
        try {
            setLoading(true);

            const userEmail = await asyncApiRequest("GET", "/api/users/${userId}");
            setUser(userEmail);

            //const activeBookingsData = await asyncApiRequest("GET", "/api/rentals/${userEmail}");
            //setActiveBookings(activeBookingsData);

            //const bookingHistoryData = await asyncApiRequest("GET", "/api/rentals/history/${userEmail}");
            //setBookingHistory(bookingHistoryData);

            const favoriteCars = await getFavorites();
            setFavoriteVehicles(favoriteCars);
        } catch (error) {
            console.error("Error fetching data:", error);
        } finally {
            setLoading(false);
        }
    }

    if (loading) {
        return <div>Loading...</div>;
    }

    if (!user) {
        return <div>Error loading user data.</div>;
    }

    return (
        <div className="flex flex-col min-h-screen">
            <header className="px-4 lg:px-6 h-16 flex items-center justify-between border-b">
                <Link className="flex items-center gap-2 font-semibold" href="/">
                    <Car className="h-6 w-6 text-emerald-600" />
                    <span>DriveEasy</span>
                </Link>
                <nav className="hidden md:flex gap-6">
                    <Link className="text-sm font-medium hover:underline underline-offset-4" href="/">
                        Home
                    </Link>
                    <Link className="text-sm font-medium hover:underline underline-offset-4" href="/cars">
                        Cars
                    </Link>
                    <Link className="text-sm font-medium hover:underline underline-offset-4" href="/locations">
                        Locations
                    </Link>
                    <Link className="text-sm font-medium hover:underline underline-offset-4" href="/about">
                        About
                    </Link>
                    <Link className="text-sm font-medium hover:underline underline-offset-4" href="/contact">
                        Contact
                    </Link>
                </nav>
                <div className="flex items-center gap-4">
                    <Button variant="ghost" size="icon">
                        <Bell className="h-5 w-5" />
                    </Button>
                    <Button variant="ghost" size="icon">
                        <Settings className="h-5 w-5" />
                    </Button>
                    <div className="h-8 w-8 rounded-full bg-emerald-100 flex items-center justify-center text-emerald-700 font-medium">
                        {user.name[0]}
                    </div>
                </div>
            </header>

            <main className="flex-1 container py-10">
                <div className="grid gap-8 md:grid-cols-[300px_1fr]">
                    {/* Profile Sidebar */}
                    <div className="space-y-6">
                        <Card>
                            <CardContent className="p-6 flex flex-col items-center text-center">
                                <div className="relative mb-4">
                                    <div className="h-24 w-24 rounded-full overflow-hidden bg-emerald-100">
                                        <Image
                                            src={user.profileImage || "/placeholder.svg"}
                                            alt={user.name}
                                            width={100}
                                            height={100}
                                            className="object-cover"
                                        />
                                    </div>
                                    <Button
                                        variant="outline"
                                        size="icon"
                                        className="absolute bottom-0 right-0 h-8 w-8 rounded-full bg-background"
                                    >
                                        <Edit className="h-4 w-4" />
                                        <span className="sr-only">Edit profile picture</span>
                                    </Button>
                                </div>
                                <h2 className="text-xl font-bold">{user.name}</h2>
                                <p className="text-sm text-muted-foreground">{user.email}</p>
                                <p className="text-sm text-muted-foreground">Member since {user.memberSince}</p>

                                <div className="mt-6 w-full">
                                    <div className="flex justify-between text-sm mb-1">
                    <span>
                      {user.loyaltyLevel} ({user.loyaltyPoints} points)
                    </span>
                                        <span>
                      {user.nextLevel}: {user.nextLevelPoints} points
                    </span>
                                    </div>
                                    <Progress value={(user.loyaltyPoints / user.nextLevelPoints) * 100} className="h-2" />
                                </div>

                                <Button asChild className="mt-6 w-full">
                                    <Link href="/profile/edit">
                                        <Edit className="mr-2 h-4 w-4" />
                                        Edit Profile
                                    </Link>
                                </Button>
                            </CardContent>
                        </Card>
                    </div>

                    {/* Main Content */}
                    <div className="space-y-6">
                        <Tabs defaultValue="bookings" className="w-full">
                            <TabsList className="w-full justify-start border-b rounded-none h-auto p-0 bg-transparent">
                                <TabsTrigger
                                    value="bookings"
                                    className="rounded-none data-[state=active]:border-b-2 data-[state=active]:border-emerald-600 data-[state=active]:shadow-none bg-transparent px-4 py-2"
                                >
                                    <Calendar className="mr-2 h-4 w-4" />
                                    Bookings
                                </TabsTrigger>
                                <TabsTrigger
                                    value="history"
                                    className="rounded-none data-[state=active]:border-b-2 data-[state=active]:border-emerald-600 data-[state=active]:shadow-none bg-transparent px-4 py-2"
                                >
                                    <History className="mr-2 h-4 w-4" />
                                    History
                                </TabsTrigger>
                                <TabsTrigger
                                    value="favorites"
                                    className="rounded-none data-[state=active]:border-b-2 data-[state=active]:border-emerald-600 data-[state=active]:shadow-none bg-transparent px-4 py-2"
                                >
                                    <Heart className="mr-2 h-4 w-4" />
                                    Favorites
                                </TabsTrigger>
                            </TabsList>

                            {/* Active Bookings */}
                            <TabsContent value="bookings" className="pt-6">
                                {/* Render active bookings */}
                            </TabsContent>

                            {/* Booking History */}
                            <TabsContent value="history" className="pt-6">
                                {/* Render booking history */}
                            </TabsContent>

                            {/* Favorite Vehicles */}
                            <TabsContent value="favorites" className="pt-6">
                                {/* Render favorite vehicles */}
                            </TabsContent>
                        </Tabs>
                    </div>
                </div>
            </main>

            <footer className="flex flex-col gap-2 sm:flex-row py-6 w-full shrink-0 items-center px-4 md:px-6 border-t">
                <p className="text-xs text-gray-500">© 2023 DriveEasy. All rights reserved.</p>
                <nav className="sm:ml-auto flex gap-4 sm:gap-6">
                    <Link className="text-xs hover:underline underline-offset-4" href="#">
                        Terms of Service
                    </Link>
                    <Link className="text-xs hover:underline underline-offset-4" href="#">
                        Privacy
                    </Link>
                    <Link className="text-xs hover:underline underline-offset-4" href="#">
                        Cookies
                    </Link>
                </nav>
            </footer>
        </div>
    );
}