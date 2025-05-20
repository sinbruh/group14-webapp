import Link from "next/link";
import { Car } from "lucide-react";
import { useEffect } from "react";
import { usePathname } from "next/navigation";
import AuthModal from "@/components/AuthModal";
import UserDropdown from "@/components/UserDropdown";
import { useStore, getAuthenticatedUser } from "@/services/authentication";

export default function Navbar() {
    const pathname = usePathname();
    const user = useStore((state) => state.user);
    const setUser = useStore((state) => state.setUser);

    // Initialize user from cookies on component mount
    useEffect(() => {
        if (!user) {
            const authenticatedUser = getAuthenticatedUser();
            if (authenticatedUser) {
                setUser(authenticatedUser);
            }
        }
    }, [user, setUser]);

    return (
        <header className="px-4 lg:px-6 h-16 flex items-center justify-between border-b">
            <Link className="flex items-center gap-2 font-semibold" href="/">
                <Car className="h-6 w-6 text-emerald-600" />
                <span>RentalRoulette</span>
            </Link>
            <nav className="hidden md:flex gap-6">
                <Link
                    className={`text-sm font-medium hover:underline underline-offset-4 ${
                        pathname === "/" ? "text-emerald-600" : ""
                    }`}
                    href="/"
                >
                    Home
                </Link>
                <Link
                    className={`text-sm font-medium hover:underline underline-offset-4 ${
                        pathname === "/cars" ? "text-emerald-600" : ""
                    }`}
                    href="/cars"
                >
                    Cars
                </Link>
                <Link
                    className={`text-sm font-medium hover:underline underline-offset-4 ${
                        pathname === "/about" ? "text-emerald-600" : ""
                    }`}
                    href="/about"
                >
                    About
                </Link>
                <Link
                    className={`text-sm font-medium hover:underline underline-offset-4 ${
                        pathname === "/contact" ? "text-emerald-600" : ""
                    }`}
                    href="/contact"
                >
                    Contact
                </Link>
            </nav>
            <div className="flex items-center gap-4">
                {user ? <UserDropdown /> : <AuthModal />}
            </div>
        </header>
    );
}