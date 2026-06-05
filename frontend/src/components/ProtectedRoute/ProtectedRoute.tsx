import { Navigate } from "react-router-dom";

interface ProtectedRouteProps {
    children: React.ReactNode;
}

const ProtectedRoute = ({ children }: ProtectedRouteProps) => {
    const userId = sessionStorage.getItem("userId");
    console.log(userId);

    return userId ? children : <Navigate to="/signin" replace />;
};

export default ProtectedRoute;