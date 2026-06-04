import './App.css'
import {Routes, Route} from 'react-router-dom'
import SignIn from "./components/SignIn/SignIn.tsx";
import Products from "./components/Products/Products.tsx";
import ProductDetail from "./components/ProductDetail/ProductDetail.tsx";

function App() {
    return (
        <Routes>
            <Route path="/" element={<SignIn/>}/>
            <Route path="/products" element={<Products/>}/>
            <Route path="/products/:id" element={<ProductDetail/>}/>
        </Routes>
    )
}

export default App
