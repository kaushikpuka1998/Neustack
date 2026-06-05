import './App.css';
import {Routes, Route} from 'react-router-dom';

import SignIn from "./components/SignIn/SignIn";
import Signup from "./components/Signup/Signup";
import Products from "./components/Products/Products";
import ProductDetail from "./components/ProductDetail/ProductDetail";
import CartList from "./components/Cart/CartList";
import OrderList from "./components/OrderList/OrderList";
import Header from "./components/Header/Header";

function App() {

    const isLoggedIn = !!localStorage.getItem("userId");
    return (
        <>
            <Header/>
            <Routes>
                <Route path="/" element={!isLoggedIn ? <SignIn/> : <Products/>}/>
                <Route path="/products" element={<Products/>}/>
                <Route path="/products/:id" element={<ProductDetail/>}/>
                <Route path="/cart" element={<CartList/>}/>
                <Route path="/signup" element={<Signup/>}/>
                <Route path="/signin" element={<SignIn/>}/>
                <Route path="/orders" element={<OrderList/>}/>
            </Routes>
        </>
    );
}

export default App;