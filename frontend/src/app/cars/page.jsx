"use client";
import Link from "next/link";
import Image from "next/image";
import { useEffect, useState } from "react";
import { Car, ChevronDown, Filter, Fuel, Search, Settings, Users } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Card, CardContent, CardFooter, CardHeader } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from "@/components/ui/accordion";
import { Checkbox } from "@/components/ui/checkbox";
import { Slider } from "@/components/ui/slider";
import { CarModal } from "@/components/carModal";
import AuthModal from "@/components/AuthModal";
import { fetchConfigurations } from "@/services/authentication";

export default function CarsPage() {
    const [cars, setCars] = useState([]);
    const [Loading, setLoading] = useState(true);
    const [selectedCar, setSelectedCar] = useState(null);
    const [isModalOpen, setIsModalOpen] = useState(false);

    const openModal = (car) => {
        setSelectedCar(car);
        setIsModalOpen(true);
    };

    const closeModal = () => {
        setSelectedCar(null);
        setIsModalOpen(false);
    };

    useEffect(() => {
        async function loadCars() {
            try {
                const configurations = await fetchConfigurations();
                setCars(configurations);
            } catch (error) {
                console.error("Error fetching configurations: ", error);
            } finally {
                setLoading(false);
            }
        }
    })

    if (loading) {
        return <div className="flex justify-center items-center h-screen">Loading...</div>;
    }

    return (
        <div className="flex flex-col min-h-screen">
            <header className="px-4 lg:px-6 h-16 flex items-center justify-between border-b">
                <Link className="flex items-center gap-2 font-semibold" href="/">
                    <Car className="h-6 w-6 text-emerald-600" />
                    <span>RentalRoulette</span>
                </Link>
                <nav className="hidden md:flex gap-6">
                    <Link className="text-sm font-medium hover:underline underline-offset-4" href="/">
                        Home
                    </Link>
                    <Link className="text-sm font-medium hover:underline underline-offset-4 text-emerald-600" href="/cars">
                        Cars
                    </Link>
                    <Link className="text-sm font-medium hover:underline underline-offset-4" href="/about">
                        About
                    </Link>
                    <Link className="text-sm font-medium hover:underline underline-offset-4" href="/contact">
                        Contact
                    </Link>
                </nav>
                <div className="flex items-center gap-4">
                    <AuthModal />
                </div>
            </header>

            <main className="flex-1">
                <section className="w-full py-12 md:py-24 lg:py-32 bg-emerald-50">
                    <div className="container px-4 md:px-6">
                        <div className="flex flex-col items-center justify-center space-y-4 text-center">
                            <div className="space-y-2">
                                <h1 className="text-3xl font-bold tracking-tighter sm:text-4xl md:text-5xl">Our Fleet</h1>
                                <p className="max-w-[700px] text-gray-500 md:text-xl">
                                    Browse our extensive selection of vehicles to find the perfect match for your needs.
                                </p>
                            </div>
                            <div className="w-full max-w-sm">
                                <div className="relative">
                                    <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-gray-500" />
                                    <Input
                                        type="search"
                                        placeholder="Search cars..."
                                        className="w-full bg-white pl-8 rounded-full"
                                    />
                                </div>
                            </div>
                        </div>
                    </div>
                </section>

                <section className="w-full py-12">
                    <div className="container px-4 md:px-6">
                        <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
                            {cars.map((car) => (
                                <Card key={car.name} className="overflow-hidden">
                                    <CardHeader className="p-0">
                                        <div className="relative">
                                            <Image
                                                src={car.image || "/placeholder.svg"}
                                                alt={car.name}
                                                width={300}
                                                height={200}
                                                className="w-full object-cover h-48"
                                            />
                                            {car.isNew && (
                                                <Badge className="absolute top-2 right-2 bg-emerald-600">New</Badge>
                                            )}
                                        </div>
                                    </CardHeader>
                                    <CardContent className="p-4">
                                        <div className="flex justify-between items-start">
                                            <div>
                                                <h3 className="font-bold">{car.name}</h3>
                                                <p className="text-sm text-gray-500">{car.make} - {car.model}</p>
                                            </div>
                                            <div className="text-right">
                                                <span className="font-bold text-lg">${car.price}</span>
                                                <p className="text-xs text-gray-500">per day</p>
                                            </div>
                                        </div>
                                        <div className="grid grid-cols-3 gap-2 mt-4 text-sm">
                                            <div className="flex flex-col items-center gap-1">
                                                <Users className="h-4 w-4 text-gray-500" />
                                                <span>{car.numberOfSeats} Seats</span>
                                            </div>
                                            <div className="flex flex-col items-center gap-1">
                                                <Settings className="h-4 w-4 text-gray-500" />
                                                <span>{car.transmissionType}</span>
                                            </div>
                                            <div className="flex flex-col items-center gap-1">
                                                <Fuel className="h-4 w-4 text-gray-500" />
                                                <span>{car.fuelType}</span>
                                            </div>
                                        </div>
                                    </CardContent>
                                    <CardFooter className="p-4 pt-0">
                                        <Button
                                            className="w-full bg-emerald-600 text-white hover:bg-emerald-700"
                                            onClick={() => openModal(car)}
                                        >
                                            View Details
                                        </Button>
                                    </CardFooter>
                                </Card>
                            ))}
                        </div>
                    </div>
                </section>
            </main>

            {isModalOpen && selectedCar && (
                <CarModal car={selectedCar} isOpen={isModalOpen} onClose={closeModal} />
            )}

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