"use client";
import Link from "next/link";
import { CalendarDays, Car, MapPin, Search, Star, Users } from "lucide-react";
import { useState } from "react";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Card, CardContent } from "@/components/ui/card";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { DatePickerWithRange } from "@/components/date-range-picker";
import { FeaturedCars } from "@/components/featured-cars";
import { Testimonials } from "@/components/testimonials";
import { SpecialOffers } from "@/components/special-offers";
import Navbar from "@/components/Navbar";
import { useRouter } from "next/navigation";

export default function Home() {
  const router = useRouter();
  const [pickupLocation, setPickupLocation] = useState("");
  const [dropoffLocation, setDropoffLocation] = useState("");
  const [dateRange, setDateRange] = useState({ from: undefined, to: undefined });
  const [carType, setCarType] = useState("");

  const handleRedirectToCars = () => {
    // Build query string with selected values
    const params = new URLSearchParams();
    if (pickupLocation) params.append("pickup", pickupLocation);
    if (dropoffLocation) params.append("dropoff", dropoffLocation);
    if (dateRange.from) params.append("from", dateRange.from.toISOString());
    if (dateRange.to) params.append("to", dateRange.to.toISOString());
    if (carType) params.append("type", carType);

    // Redirect to cars page with query parameters
    router.push(`/cars?${params.toString()}`);
  };

  return (
    <div className="flex flex-col min-h-screen">
      <Navbar />
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
                    Discover our wide selection of vehicles for any occasion.
                    Easy booking, flexible options, and competitive rates.
                  </p>
                </div>
                <div className="flex flex-col gap-2 min-[400px]:flex-row">
                  <Button
                    size="lg"
                    className="bg-emerald-600 hover:bg-emerald-700"
                    onClick={handleRedirectToCars}
                  >
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
                    <div className="mt-4 space-y-4">
                        <div className="grid gap-4">
                          <div className="flex items-center gap-2 rounded-md border p-2">
                            <MapPin className="h-4 w-4 text-gray-500" />
                            <Select onValueChange={setPickupLocation}>
                              <SelectTrigger className="border-0 p-0 shadow-none focus:ring-0">
                                <SelectValue placeholder="Pickup Location" />
                              </SelectTrigger>
                              <SelectContent>
                                <SelectItem value="nyc">
                                  New York City
                                </SelectItem>
                                <SelectItem value="la">Los Angeles</SelectItem>
                                <SelectItem value="chicago">Chicago</SelectItem>
                                <SelectItem value="miami">Miami</SelectItem>
                              </SelectContent>
                            </Select>
                          </div>
                          <div className="flex items-center gap-2 rounded-md border p-2">
                            <MapPin className="h-4 w-4 text-gray-500" />
                            <Select onValueChange={setDropoffLocation}>
                              <SelectTrigger className="border-0 p-0 shadow-none focus:ring-0">
                                <SelectValue placeholder="Drop-off Location" />
                              </SelectTrigger>
                              <SelectContent>
                                <SelectItem value="same">
                                  Same as pickup
                                </SelectItem>
                                <SelectItem value="nyc">
                                  New York City
                                </SelectItem>
                                <SelectItem value="la">Los Angeles</SelectItem>
                                <SelectItem value="chicago">Chicago</SelectItem>
                                <SelectItem value="miami">Miami</SelectItem>
                              </SelectContent>
                            </Select>
                          </div>
                          <div className="rounded-md border p-2">
                            <DatePickerWithRange className="border-0 p-0 shadow-none" value={dateRange} onChange={setDateRange} />
                          </div>
                          <div className="flex items-center gap-2 rounded-md border p-2">
                            <Car className="h-4 w-4 text-gray-500" />
                            <Select onValueChange={setCarType}>
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
                          <Button className="w-full bg-emerald-600 hover:bg-emerald-700" onClick={handleRedirectToCars}>
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

        {/*<FeaturedCars />*/}

        {/*<SpecialOffers />*/}

        <section className="w-full py-12 md:py-24 lg:py-32 bg-gray-50">
          <div className="container px-4 md:px-6">
            <div className="flex flex-col items-center justify-center space-y-4 text-center">
              <div className="space-y-2">
                <div className="inline-block rounded-lg bg-emerald-100 px-3 py-1 text-sm text-emerald-700">
                  Why Choose Us
                </div>
                <h2 className="text-3xl font-bold tracking-tighter md:text-4xl">
                  The RentalRoulette Advantage
                </h2>
                <p className="max-w-[900px] text-gray-500 md:text-xl/relaxed lg:text-base/relaxed xl:text-xl/relaxed">
                  We offer more than just car rentals. Experience the difference
                  with our premium service.
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
                  Our vehicles are regularly maintained and replaced to ensure
                  you always get a reliable, up-to-date car.
                </p>
              </div>
              <div className="grid gap-1 text-center">
                <div className="flex h-20 w-20 items-center justify-center rounded-full bg-emerald-100 mx-auto">
                  <MapPin className="h-10 w-10 text-emerald-600" />
                </div>
                <h3 className="text-xl font-bold">Convenient Locations</h3>
                <p className="text-gray-500">
                  With pickup points across the country, you can find us
                  wherever your journey takes you.
                </p>
              </div>
              <div className="grid gap-1 text-center">
                <div className="flex h-20 w-20 items-center justify-center rounded-full bg-emerald-100 mx-auto">
                  <Star className="h-10 w-10 text-emerald-600" />
                </div>
                <h3 className="text-xl font-bold">Premium Service</h3>
                <p className="text-gray-500">
                  Our dedicated team ensures your rental experience is smooth
                  and hassle-free from start to finish.
                </p>
              </div>
            </div>
          </div>
        </section>

        <Testimonials />
      </main>
      <footer className="flex flex-col gap-2 sm:flex-row py-6 w-full shrink-0 items-center px-4 md:px-6 border-t">
        <p className="text-xs text-gray-500">
          © 2023 RentalRoulette. All rights reserved.
        </p>
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
