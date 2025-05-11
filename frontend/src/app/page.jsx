import Link from "next/link";
import Image from "next/image";
import { CalendarDays, Car, MapPin, Search, Star, Users } from "lucide-react";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Card, CardContent } from "@/components/ui/card";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { DatePickerWithRange } from "@/components/date-range-picker";
import { FeaturedCars } from "@/components/featured-cars";
import { Testimonials } from "@/components/testimonials";
import { SpecialOffers } from "@/components/special-offers";
import AuthModal from "@/components/AuthModal";

export default function Home() {
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
                    <AuthModal />
                </div>
            </header>
            <main className="flex-1">
                <section className="w-full py-12 md:py-24 lg:py-32 bg-gradient-to-b from-emerald-50 to-white">
                    <div className="container px-4 md:px-6">
                        <div className="grid gap-6 lg:grid-cols-[1fr_400px] lg:gap-12 xl:grid-cols-[1fr_600px]">
                            <div className="flex flex-col justify-center space-y-4">
                                <div className="space-y-2">
                                    <h1 className="text-3xl font-bold tracking-tighter sm:text-5xl xl:text-6xl/none">
                                        Find Your Perfect Ride
                                    </h1>
                                    <p className="max-w-[600px] text-gray-500 md:text-xl">
                                        Discover our wide selection of vehicles for any occasion. Easy booking, flexible options, and
                                        competitive rates.
                                    </p>
                                </div>
                                <div className="flex flex-col gap-2 min-[400px]:flex-row">
                                    <Button size="lg" className="bg-emerald-600 hover:bg-emerald-700">
                                        Browse Cars
                                    </Button>
                                    <Button size="lg" variant="outline">
                                        Special Offers
                                    </Button>
                                </div>
                            </div>
                            <div className="mx-auto w-full max-w-[500px] lg:max-w-none">
                                <Card className="p-4 shadow-lg">
                                    <CardContent className="p-0">
                                        <Tabs defaultValue="rental" className="w-full">
                                            <TabsList className="grid w-full grid-cols-2">
                                                <TabsTrigger value="rental">Car Rental</TabsTrigger>
                                                <TabsTrigger value="airport">Airport Transfer</TabsTrigger>
                                            </TabsList>
                                            <TabsContent value="rental" className="mt-4 space-y-4">
                                                <div className="grid gap-4">
                                                    <div className="flex items-center gap-2 rounded-md border p-2">
                                                        <MapPin className="h-4 w-4 text-gray-500" />
                                                        <Select>
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
                                                    <div className="flex items-center gap-2 rounded-md border p-2">
                                                        <MapPin className="h-4 w-4 text-gray-500" />
                                                        <Select>
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
                                                    <div className="rounded-md border p-2">
                                                        <DatePickerWithRange className="border-0 p-0 shadow-none" />
                                                    </div>
                                                    <div className="flex items-center gap-2 rounded-md border p-2">
                                                        <Car className="h-4 w-4 text-gray-500" />
                                                        <Select>
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
                                                    <Button className="w-full bg-emerald-600 hover:bg-emerald-700">
                                                        <Search className="mr-2 h-4 w-4" />
                                                        Search Cars
                                                    </Button>
                                                </div>
                                            </TabsContent>
                                            <TabsContent value="airport" className="mt-4 space-y-4">
                                                <div className="grid gap-4">
                                                    <div className="flex items-center gap-2 rounded-md border p-2">
                                                        <MapPin className="h-4 w-4 text-gray-500" />
                                                        <Input className="border-0 p-0 shadow-none focus-visible:ring-0" placeholder="Airport" />
                                                    </div>
                                                    <div className="flex items-center gap-2 rounded-md border p-2">
                                                        <MapPin className="h-4 w-4 text-gray-500" />
                                                        <Input
                                                            className="border-0 p-0 shadow-none focus-visible:ring-0"
                                                            placeholder="Destination"
                                                        />
                                                    </div>
                                                    <div className="rounded-md border p-2">
                                                        <div className="flex items-center gap-2">
                                                            <CalendarDays className="h-4 w-4 text-gray-500" />
                                                            <Input
                                                                className="border-0 p-0 shadow-none focus-visible:ring-0"
                                                                type="date"
                                                                placeholder="Date"
                                                            />
                                                        </div>
                                                    </div>
                                                    <div className="flex items-center gap-2 rounded-md border p-2">
                                                        <Users className="h-4 w-4 text-gray-500" />
                                                        <Select>
                                                            <SelectTrigger className="border-0 p-0 shadow-none focus:ring-0">
                                                                <SelectValue placeholder="Passengers" />
                                                            </SelectTrigger>
                                                            <SelectContent>
                                                                <SelectItem value="1">1 Passenger</SelectItem>
                                                                <SelectItem value="2">2 Passengers</SelectItem>
                                                                <SelectItem value="3">3 Passengers</SelectItem>
                                                                <SelectItem value="4">4+ Passengers</SelectItem>
                                                            </SelectContent>
                                                        </Select>
                                                    </div>
                                                    <Button className="w-full bg-emerald-600 hover:bg-emerald-700">
                                                        <Search className="mr-2 h-4 w-4" />
                                                        Find Transfer
                                                    </Button>
                                                </div>
                                            </TabsContent>
                                        </Tabs>
                                    </CardContent>
                                </Card>
                            </div>
                        </div>
                    </div>
                </section>

                <FeaturedCars />

                <SpecialOffers />

                <section className="w-full py-12 md:py-24 lg:py-32 bg-gray-50">
                    <div className="container px-4 md:px-6">
                        <div className="flex flex-col items-center justify-center space-y-4 text-center">
                            <div className="space-y-2">
                                <div className="inline-block rounded-lg bg-emerald-100 px-3 py-1 text-sm text-emerald-700">
                                    Why Choose Us
                                </div>
                                <h2 className="text-3xl font-bold tracking-tighter md:text-4xl">The DriveEasy Advantage</h2>
                                <p className="max-w-[900px] text-gray-500 md:text-xl/relaxed lg:text-base/relaxed xl:text-xl/relaxed">
                                    We offer more than just car rentals. Experience the difference with our premium service.
                                </p>
                            </div>
                        </div>
                        <div className="mx-auto grid max-w-5xl items-center gap-6 py-12 lg:grid-cols-3 lg:gap-12">
                            <div className="grid gap-1 text-center">
                                <div className="flex h-20 w-20 items-center justify-center rounded-full bg-emerald-100 mx-auto">
                                    <Car className="h-10 w-10 text-emerald-600" />
                                </div>
                                <h3 className="text-xl font-bold">Modern Fleet</h3>
                                <p className="text-gray-500">
                                    Our vehicles are regularly maintained and replaced to ensure you always get a reliable, up-to-date
                                    car.
                                </p>
                            </div>
                            <div className="grid gap-1 text-center">
                                <div className="flex h-20 w-20 items-center justify-center rounded-full bg-emerald-100 mx-auto">
                                    <MapPin className="h-10 w-10 text-emerald-600" />
                                </div>
                                <h3 className="text-xl font-bold">Convenient Locations</h3>
                                <p className="text-gray-500">
                                    With pickup points across the country, you can find us wherever your journey takes you.
                                </p>
                            </div>
                            <div className="grid gap-1 text-center">
                                <div className="flex h-20 w-20 items-center justify-center rounded-full bg-emerald-100 mx-auto">
                                    <Star className="h-10 w-10 text-emerald-600" />
                                </div>
                                <h3 className="text-xl font-bold">Premium Service</h3>
                                <p className="text-gray-500">
                                    Our dedicated team ensures your rental experience is smooth and hassle-free from start to finish.
                                </p>
                            </div>
                        </div>
                    </div>
                </section>

                <Testimonials />

                <section className="w-full py-12 md:py-24 lg:py-32 border-t">
                    <div className="container px-4 md:px-6">
                        <div className="grid gap-10 px-10 md:gap-16 lg:grid-cols-2">
                            <div className="flex flex-col justify-center space-y-4">
                                <div className="space-y-2">
                                    <h2 className="text-3xl font-bold tracking-tighter md:text-4xl">Download Our Mobile App</h2>
                                    <p className="max-w-[600px] text-gray-500 md:text-xl">
                                        Get exclusive deals and manage your bookings on the go with our easy-to-use mobile application.
                                    </p>
                                </div>
                                <div className="flex flex-col gap-2 min-[400px]:flex-row">
                                    <Button variant="outline" size="lg" className="gap-2">
                                        <Image src="/placeholder.svg?height=24&width=24" width={24} height={24} alt="App Store" />
                                        App Store
                                    </Button>
                                    <Button variant="outline" size="lg" className="gap-2">
                                        <Image src="/placeholder.svg?height=24&width=24" width={24} height={24} alt="Google Play" />
                                        Google Play
                                    </Button>
                                </div>
                            </div>
                            <div className="mx-auto w-full max-w-[400px] lg:max-w-none">
                                <Image
                                    src="/placeholder.svg?height=500&width=400"
                                    width={400}
                                    height={500}
                                    alt="Mobile App"
                                    className="mx-auto aspect-auto overflow-hidden rounded-xl object-cover object-center sm:w-full"
                                />
                            </div>
                        </div>
                    </div>
                </section>
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
    )
}