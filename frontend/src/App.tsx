import './App.css'
import {Routes, Route} from 'react-router-dom'
import SignIn from "./components/SignIn/SignIn.tsx";
import Products from "./components/Products/Products.tsx";
import ProductDetail from "./components/ProductDetail/ProductDetail.tsx";
import Header from "./components/Header/Header.tsx";
import CartList from "./components/Cart/CartList.tsx";
import Signup from "./components/Signup/Signup.tsx";
import OrderList from "./components/OrderList/OrderList.tsx";

function App() {
    return (
        <>
            <Header/>
            <Routes>
                <Route path="/" element={<SignIn/>}/>
                <Route path="/products" element={<Products/>}/>
                <Route path="/products/:id" element={<ProductDetail/>}/>
                <Route path="/cart" element={<CartList/>}/>
                <Route path="/signup" element={<Signup/>}/>
                <Route path="/signin" element={<SignIn/>}/>
                <Route path="/orders" element={<OrderList/>}/>
            </Routes>
        </>
    )
}

export default App
