import Image from "next/image";

export default function About() {
    return (
        <div className="container mx-auto py-12 px-4">
            <h1 className="text-3xl font-bold mb-6">About Us</h1>
            <div className="grid gap-6 md:grid-cols-2">
                <div>
                    <p className="text-gray-600">
                        Welcome to DriveEasy! We are dedicated to providing the best car rental experience with a modern fleet, convenient locations, and premium service.
                    </p>
                </div>
                <div>
                    <Image
                        src="/placeholder.svg?height=400&width=600"
                        width={600}
                        height={400}
                        alt="About Us"
                        className="rounded-lg"
                    />
                </div>
            </div>
        </div>
    );
}