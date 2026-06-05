import './App.css';
import { Routes, Route, Navigate } from 'react-router-dom';

import SignIn from "./components/SignIn/SignIn";
import Signup from "./components/Signup/Signup";
import Products from "./components/Products/Products";
import ProductDetail from "./components/ProductDetail/ProductDetail";
import CartList from "./components/Cart/CartList";
import OrderList from "./components/OrderList/OrderList";
import Header from "./components/Header/Header";
import ProtectedRoute from "./components/ProtectedRoute/ProtectedRoute.tsx";

function App() {

    const isLoggedIn = !! localStorage.getItem("userId");

    return (
        <>
            <Header />

            <Routes>

                {/* Default Route */}
                <Route
                    path="/"
                    element={
                        isLoggedIn
                            ? <Navigate to="/products" replace />
                            : <Navigate to="/signin" replace />
                    }
                />

                {/* Public Routes */}
                <Route path="/signin" element={<SignIn />} />
                <Route path="/signup" element={<Signup />} />

                {/* Protected Routes */}
                <Route
                    path="/products"
                    element={
                        <ProtectedRoute>
                            <Products />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/products/:id"
                    element={
                        <ProtectedRoute>
                            <ProductDetail />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/cart"
                    element={
                        <ProtectedRoute>
                            <CartList />
                        </ProtectedRoute>
                    }
                />

                <Route
                    path="/orders"
                    element={
                        <ProtectedRoute>
                            <OrderList />
                        </ProtectedRoute>
                    }
                />
            </Routes>
        </>
    );
}

export default App;