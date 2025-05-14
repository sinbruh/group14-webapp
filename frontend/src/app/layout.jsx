import React from "react"
import { Inter } from "next/font/google"
import "./globals.css"

const inter = Inter({ subsets: ["latin"] })

export const metadata = {
    title: "DriveEasy - Car Rental Service",
    description: "Find your perfect ride with our premium car rental service",
}

export default function RootLayout({ children }) {
    return (
        <html lang="en">
        <body className={inter.className}>{children}</body>
        </html>
    )
}