"use client";
import { useRouter } from "next/navigation";
import { Button } from "@/components/ui/button";

export default function AboutPage() {
    const router = useRouter();

    const handleRedirect = () => {
        router.push("/");
    };

    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-50">
            <h1 className="text-4xl font-bold mb-4">About Us</h1>
            <p className="text-gray-600 text-center max-w-md mb-6">
                Welcome to DriveEasy! We are committed to providing the best car rental experience with a wide range of vehicles to suit your needs.
            </p>
            <Button className="bg-emerald-600 text-white hover:bg-emerald-700" onClick={handleRedirect}>
                Go Back to Home
            </Button>
        </div>
    );
}