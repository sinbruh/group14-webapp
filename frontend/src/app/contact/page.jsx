"use client";
import Link from "next/link";
import { Car } from "lucide-react";

export default function ContactPage() {
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
                    <Link className="text-sm font-medium hover:underline underline-offset-4" href="/about">
                        About
                    </Link>
                    <Link className="text-sm font-medium hover:underline underline-offset-4 text-emerald-600" href="/contact">
                        Contact
                    </Link>
                </nav>
                <div className="flex items-center gap-4">
                    <Link className="text-sm font-medium hover:underline underline-offset-4" href="/login">
                        Log In
                    </Link>
                    <button className="px-4 py-2 bg-emerald-600 text-white rounded hover:bg-emerald-700">
                        <Link href="/register">Sign Up</Link>
                    </button>
                </div>
            </header>
            <main className="container mx-auto px-4 py-8">
                <h1 className="text-4xl font-bold text-emerald-600 mb-4">Contact Us</h1>
                <p className="text-lg text-gray-600 leading-relaxed">
                    Have questions or need assistance? We're here to help! Reach out to us through any of the methods below.
                </p>
                <div className="mt-8 grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div className="p-6 bg-gray-100 rounded-lg shadow">
                        <h2 className="text-2xl font-semibold mb-2">Email</h2>
                        <p className="text-gray-600">support@rentalroulette.com</p>
                    </div>
                    <div className="p-6 bg-gray-100 rounded-lg shadow">
                        <h2 className="text-2xl font-semibold mb-2">Phone</h2>
                        <p className="text-gray-600">+1 (800) 123-4567</p>
                    </div>
                    <div className="p-6 bg-gray-100 rounded-lg shadow">
                        <h2 className="text-2xl font-semibold mb-2">Address</h2>
                        <p className="text-gray-600">
                            Placeholder Address<br />
                        </p>
                    </div>
                    <div className="p-6 bg-gray-100 rounded-lg shadow">
                        <h2 className="text-2xl font-semibold mb-2">Social Media</h2>
                        <p className="text-gray-600">
                            Follow us on <Link href="#" className="text-emerald-600 hover:underline">Twitter</Link>, <Link href="#" className="text-emerald-600 hover:underline">Facebook</Link>, and <Link href="#" className="text-emerald-600 hover:underline">Instagram</Link>.
                        </p>
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