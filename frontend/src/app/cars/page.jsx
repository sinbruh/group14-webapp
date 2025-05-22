"use client";
import Link from "next/link";
import Image from "next/image";
import { useState, useEffect } from "react";
import { ChevronDown, Filter, Fuel, Search, Settings, Users, MapPin, Car as CarIcon } from "lucide-react";
import { useSearchParams } from "next/navigation";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Card, CardContent, CardFooter, CardHeader } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Accordion, AccordionContent, AccordionItem, AccordionTrigger } from "@/components/ui/accordion";
import { Checkbox } from "@/components/ui/checkbox";
import { Slider } from "@/components/ui/slider";
import { DatePickerWithRange } from "@/components/date-range-picker";
import { CarModal } from "@/components/carModal";
import Navbar from "@/components/Navbar";
import { fetchAllCars} from "@/services/car";
import {flattenCars} from "@/lib/utils";

export default function CarsPage() {
    const searchParams = useSearchParams();

    // Get search parameters from URL
    const pickupParam = searchParams.get("pickup");
    const dropoffParam = searchParams.get("dropoff");
    const fromParam = searchParams.get("from");
    const toParam = searchParams.get("to");
    const typeParam = searchParams.get("type");

    const [selectedCar, setSelectedCar] = useState(null);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [pickupLocation, setPickupLocation] = useState(pickupParam || "");
    const [dropoffLocation, setDropoffLocation] = useState(dropoffParam || "");
    const [dateRange, setDateRange] = useState({ 
        from: fromParam ? new Date(fromParam) : undefined, 
        to: toParam ? new Date(toParam) : undefined 
    });
    const [carType, setCarType] = useState(typeParam || "");
    const [filteredCars, setFilteredCars] = useState([]);
    const [isFiltering, setIsFiltering] = useState(false);

    const openModal = (car) => {
        setSelectedCar(car);
        setIsModalOpen(true);
    };

    const closeModal = () => {
        setSelectedCar(null);
        setIsModalOpen(false);
    };

    const handleSearch = () => {
        setIsFiltering(true);
        // Basic filtering logic - can be expanded later
        const filtered = cars.filter(car => {
            let match = true;
            if (carType && car.category) {
                match = match && car.category.toLowerCase().includes(carType.toLowerCase());
            }
            return match;
        });
        setFilteredCars(filtered);
        console.log("Search criteria:", { pickupLocation, dropoffLocation, dateRange, carType });
        console.log("Filtered cars:", filtered.length);
    };

    const [cars, setCars] = useState([]);

    useEffect(() => {
        const loadCars = async () => {
            try {
                let carsData = await fetchAllCars();
                carsData = flattenCars(carsData)

                setCars(carsData);

                // If we have search parameters, filter the cars
                if (pickupParam || dropoffParam || fromParam || toParam || typeParam) {
                    const filtered = carsData.filter(car => {
                        let match = true;
                        if (typeParam && car.category) {
                            match = match && car.category.toLowerCase().includes(typeParam.toLowerCase());
                        }
                        return match;
                    });
                    setFilteredCars(filtered);
                    setIsFiltering(true);
                    console.log("Filtered cars based on URL params:", filtered.length);
                } else {
                    setFilteredCars(carsData); // Initialize filteredCars with all cars
                }

                console.log('Cars loaded:', carsData);
            } catch (error) {
                console.error('Error loading cars:', error);
            }
        };

        loadCars();
    }, [pickupParam, dropoffParam, fromParam, toParam, typeParam]);

    return (
        <div className="flex flex-col min-h-screen">
            <Navbar />

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
                            <div className="w-full max-w-3xl mx-auto">
                                <Card className="p-4 shadow-lg">
                                    <CardContent className="p-0">
                                        <div className="mt-4">
                                            <div className="flex flex-col md:flex-row flex-wrap gap-2">
                                                <div className="flex items-center gap-2 rounded-md border p-2 flex-1 min-w-[200px]">
                                                    <MapPin className="h-4 w-4 text-gray-500" />
                                                    <Select onValueChange={setPickupLocation} defaultValue={pickupLocation}>
                                                        <SelectTrigger className="border-0 p-0 shadow-none focus:ring-0">
                                                            <SelectValue placeholder="Pickup Location" />
                                                        </SelectTrigger>
                                                        <SelectContent>
                                                            <SelectItem value="nyc">New York City</SelectItem>
                                                            <SelectItem value="la">Los Angeles</SelectItem>
                                                            <SelectItem value="chicago">Chicago</SelectItem>
                                                            <SelectItem value="miami">Miami</SelectItem>
                                                        </SelectContent>
                                                    </Select>
                                                </div>
                                                <div className="flex items-center gap-2 rounded-md border p-2 flex-1 min-w-[200px]">
                                                    <MapPin className="h-4 w-4 text-gray-500" />
                                                    <Select onValueChange={setDropoffLocation} defaultValue={dropoffLocation}>
                                                        <SelectTrigger className="border-0 p-0 shadow-none focus:ring-0">
                                                            <SelectValue placeholder="Drop-off Location" />
                                                        </SelectTrigger>
                                                        <SelectContent>
                                                            <SelectItem value="same">Same as pickup</SelectItem>
                                                            <SelectItem value="nyc">New York City</SelectItem>
                                                            <SelectItem value="la">Los Angeles</SelectItem>
                                                            <SelectItem value="chicago">Chicago</SelectItem>
                                                            <SelectItem value="miami">Miami</SelectItem>
                                                        </SelectContent>
                                                    </Select>
                                                </div>
                                                <div className="rounded-md border p-2 flex-1 min-w-[200px]">
                                                    <DatePickerWithRange className="border-0 p-0 shadow-none" value={dateRange} onChange={setDateRange} />
                                                </div>
                                                <div className="flex items-center gap-2 rounded-md border p-2 flex-1 min-w-[200px]">
                                                    <CarIcon className="h-4 w-4 text-gray-500" />
                                                    <Select onValueChange={setCarType} defaultValue={carType}>
                                                        <SelectTrigger className="border-0 p-0 shadow-none focus:ring-0">
                                                            <SelectValue placeholder="Car Type" />
                                                        </SelectTrigger>
                                                        <SelectContent>
                                                            <SelectItem value="economy">Economy</SelectItem>
                                                            <SelectItem value="compact">Compact</SelectItem>
                                                            <SelectItem value="midsize">Midsize</SelectItem>
                                                            <SelectItem value="suv">SUV</SelectItem>
                                                            <SelectItem value="luxury">Luxury</SelectItem>
                                                        </SelectContent>
                                                    </Select>
                                                </div>
                                            </div>
                                            <div className="mt-2">
                                                <Button className="w-full bg-emerald-600 hover:bg-emerald-700" onClick={handleSearch}>
                                                    <Search className="mr-2 h-4 w-4" />
                                                    Search Cars
                                                </Button>
                                            </div>
                                        </div>
                                    </CardContent>
                                </Card>
                            </div>
                        </div>
                    </div>
                </section>

                <section className="w-full py-12">
                    <div className="container px-4 md:px-6">
                        <div className="grid gap-6 lg:grid-cols-[250px_1fr] lg:gap-12">
                            <div className="hidden lg:block">
                                <div className="sticky top-6">
                                    <div className="flex items-center justify-between mb-4">
                                        <h3 className="font-semibold text-lg">Filters</h3>
                                        <Button variant="ghost" size="sm" className="h-8 text-sm">
                                            Reset All
                                        </Button>
                                    </div>
                                    <Accordion type="multiple" defaultValue={["category", "price", "features"]}>
                                        <AccordionItem value="category">
                                            <AccordionTrigger>Vehicle Type</AccordionTrigger>
                                            <AccordionContent>
                                                <div className="space-y-2">
                                                    <div className="flex items-center space-x-2">
                                                        <Checkbox id="sedan" />
                                                        <label
                                                            htmlFor="sedan"
                                                            className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
                                                        >
                                                            Sedan
                                                        </label>
                                                    </div>
                                                    <div className="flex items-center space-x-2">
                                                        <Checkbox id="suv" />
                                                        <label
                                                            htmlFor="suv"
                                                            className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
                                                        >
                                                            SUV
                                                        </label>
                                                    </div>
                                                    <div className="flex items-center space-x-2">
                                                        <Checkbox id="luxury" />
                                                        <label
                                                            htmlFor="luxury"
                                                            className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
                                                        >
                                                            Luxury
                                                        </label>
                                                    </div>
                                                    <div className="flex items-center space-x-2">
                                                        <Checkbox id="electric" />
                                                        <label
                                                            htmlFor="electric"
                                                            className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
                                                        >
                                                            Electric
                                                        </label>
                                                    </div>
                                                    <div className="flex items-center space-x-2">
                                                        <Checkbox id="sports" />
                                                        <label
                                                            htmlFor="sports"
                                                            className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
                                                        >
                                                            Sports
                                                        </label>
                                                    </div>
                                                </div>
                                            </AccordionContent>
                                        </AccordionItem>
                                        <AccordionItem value="price">
                                            <AccordionTrigger>Price Range</AccordionTrigger>
                                            <AccordionContent>
                                                <div className="space-y-4">
                                                    <Slider defaultValue={[40, 120]} min={0} max={200} step={5} />
                                                    <div className="flex items-center justify-between">
                                                        <span className="text-sm">$40</span>
                                                        <span className="text-sm">$120</span>
                                                    </div>
                                                </div>
                                            </AccordionContent>
                                        </AccordionItem>
                                        <AccordionItem value="features">
                                            <AccordionTrigger>Features</AccordionTrigger>
                                            <AccordionContent>
                                                <div className="space-y-2">
                                                    <div className="flex items-center space-x-2">
                                                        <Checkbox id="automatic" />
                                                        <label
                                                            htmlFor="automatic"
                                                            className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
                                                        >
                                                            Automatic
                                                        </label>
                                                    </div>
                                                    <div className="flex items-center space-x-2">
                                                        <Checkbox id="manual" />
                                                        <label
                                                            htmlFor="manual"
                                                            className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
                                                        >
                                                            Manual
                                                        </label>
                                                    </div>
                                                    <div className="flex items-center space-x-2">
                                                        <Checkbox id="gps" />
                                                        <label
                                                            htmlFor="gps"
                                                            className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
                                                        >
                                                            GPS
                                                        </label>
                                                    </div>
                                                    <div className="flex items-center space-x-2">
                                                        <Checkbox id="bluetooth" />
                                                        <label
                                                            htmlFor="bluetooth"
                                                            className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
                                                        >
                                                            Bluetooth
                                                        </label>
                                                    </div>
                                                    <div className="flex items-center space-x-2">
                                                        <Checkbox id="child-seat" />
                                                        <label
                                                            htmlFor="child-seat"
                                                            className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
                                                        >
                                                            Child Seat
                                                        </label>
                                                    </div>
                                                </div>
                                            </AccordionContent>
                                        </AccordionItem>
                                    </Accordion>
                                </div>
                            </div>
                            <div>
                                <div className="flex flex-col gap-4 mb-6">
                                    <div className="flex items-center justify-between">
                                        <div>
                                            <h2 className="text-2xl font-bold">Available Cars</h2>
                                            <p className="text-sm text-gray-500">Showing {isFiltering ? filteredCars.length : cars.length} results</p>
                                        </div>
                                        <div className="flex items-center gap-2">
                                            <Button variant="outline" size="sm" className="lg:hidden">
                                                <Filter className="h-4 w-4 mr-2" />
                                                Filters
                                            </Button>
                                            <Select defaultValue="recommended">
                                                <SelectTrigger className="w-[180px]">
                                                    <SelectValue placeholder="Sort by" />
                                                </SelectTrigger>
                                                <SelectContent>
                                                    <SelectItem value="recommended">Recommended</SelectItem>
                                                    <SelectItem value="price-low">Price: Low to High</SelectItem>
                                                    <SelectItem value="price-high">Price: High to Low</SelectItem>
                                                    <SelectItem value="newest">Newest First</SelectItem>
                                                </SelectContent>
                                            </Select>
                                        </div>
                                    </div>
                                    <div className="flex flex-wrap gap-2">
                                        <Badge variant="outline" className="rounded-full">
                                            Sedan
                                            <Button variant="ghost" size="icon" className="h-4 w-4 ml-1 hover:bg-transparent">
                                                <ChevronDown className="h-3 w-3" />
                                            </Button>
                                        </Badge>
                                        <Badge variant="outline" className="rounded-full">
                                            Price: $40 - $120
                                            <Button variant="ghost" size="icon" className="h-4 w-4 ml-1 hover:bg-transparent">
                                                <ChevronDown className="h-3 w-3" />
                                            </Button>
                                        </Badge>
                                        <Badge variant="outline" className="rounded-full">
                                            Automatic
                                            <Button variant="ghost" size="icon" className="h-4 w-4 ml-1 hover:bg-transparent">
                                                <ChevronDown className="h-3 w-3" />
                                            </Button>
                                        </Badge>
                                    </div>
                                </div>
                                <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3">
                                    {(isFiltering ? filteredCars : cars).map((car) => (
                                        <Card key={`${car.id}/${car.configuration.id}`} className="overflow-hidden">
                                            <CardHeader className="p-0">
                                                <div className="relative">
                                                    <Image
                                                        src={`/carsLowResWEBP/${car.id}.webp`}
                                                        alt={car.make + " " + car.model}
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
                                                        <h3 className="font-bold">{car.make + " " + car.model}</h3>
                                                        <p className="text-sm text-gray-500">{car.category || ""}</p>
                                                    </div>
                                                    <div className="text-right">
                                                        <span className="font-bold text-lg">${car.price || ""}</span>
                                                        <p className="text-xs text-gray-500">per day</p>
                                                    </div>
                                                </div>
                                                <div className="grid grid-cols-3 gap-2 mt-4 text-sm">
                                                    <div className="flex flex-col items-center gap-1">
                                                        <Users className="h-4 w-4 text-gray-500" />
                                                        <span>{car.configuration.numberOfSeats} Seats</span>
                                                    </div>
                                                    <div className="flex flex-col items-center gap-1">
                                                        <Settings className="h-4 w-4 text-gray-500" />
                                                        <span>{car.configuration.transmissionType}</span>
                                                    </div>
                                                    <div className="flex flex-col items-center gap-1">
                                                        <Fuel className="h-4 w-4 text-gray-500" />
                                                        <span>{car.configuration.fuelType}</span>
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
                                <div className="flex justify-center mt-8">
                                    <div className="flex items-center gap-2">
                                        <Button variant="outline" size="icon" disabled>
                                            <ChevronDown className="h-4 w-4 rotate-90" />
                                        </Button>
                                        <Button variant="outline" size="sm" className="h-8 w-8">
                                            1
                                        </Button>
                                        <Button variant="outline" size="sm" className="h-8 w-8">
                                            2
                                        </Button>
                                        <Button variant="outline" size="sm" className="h-8 w-8">
                                            3
                                        </Button>
                                        <Button variant="outline" size="icon">
                                            <ChevronDown className="h-4 w-4 -rotate-90" />
                                        </Button>
                                    </div>
                                </div>
                            </div>
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
