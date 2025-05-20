"use client";
import Link from "next/link";
import Navbar from "@/components/Navbar";

export default function AboutPage() {

    return (
        <div className="flex flex-col min-h-screen">
            <Navbar />
            <main className="container mx-auto px-4 py-8">
                <h1 className="text-4xl font-bold text-emerald-600 mb-4">About Us</h1>
                <p className="text-lg text-gray-600 leading-relaxed">
                    Welcome to RentalRoulette\! We are committed to providing the best car rental experience
                    for our customers. Our mission is to make car rentals simple, affordable, and accessible
                    for everyone. Whether you\`re planning a road trip or need a car for daily use,
                    RentalRoulette has got you covered.
                </p>
                <div className="mt-8 grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div className="p-6 bg-gray-100 rounded-lg shadow">
                        <h2 className="text-2xl font-semibold mb-2">Our Mission</h2>
                        <p className="text-gray-600">
                            To provide reliable and affordable car rental services while ensuring customer
                            satisfaction and convenience.
                        </p>
                    </div>
                    <div className="p-6 bg-gray-100 rounded-lg shadow">
                        <h2 className="text-2xl font-semibold mb-2">Why Choose Us\?</h2>
                        <ul className="list-disc list-inside text-gray-600">
                            <li>Wide range of vehicles to choose from</li>
                            <li>Affordable pricing</li>
                            <li>24/7 customer support</li>
                            <li>Easy booking process</li>
                        </ul>
                    </div>
                </div>
            </main>
            <footer className="flex flex-col gap-2 sm:flex-row py-6 w-full shrink-0 items-center px-4 md:px-6 border-t">
                <p className="text-xs text-gray-500">© 2023 RentalRoulette. All rights reserved.</p>
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
