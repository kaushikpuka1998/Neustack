import { type FC, useEffect, useState } from 'react';
import { Navbar, Container, Nav, Badge } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { api } from '../../config';

const Header: FC = () => {
  const [count, setCount] = useState<number>(0);
  const navigate = useNavigate();

  const readCartCount = async () => {
    const userId = localStorage.getItem('userId');
    if (!userId) {
      setCount(0);
      return;
    }
    try {
      const res = await api.get(`/cart/${userId}`);
      const data = res.data;
      let items = [] as any[];
      if (Array.isArray(data)) items = data;
      else if (data && Array.isArray(data.items)) items = data.items;
      else if (data && Array.isArray(data.cart)) items = data.cart;
      else if (data && Array.isArray(data.data)) items = data.data;
      else {
        // try to find array in response
        const arrayVals = Object.values(data).filter((v) => Array.isArray(v));
        if (arrayVals.length > 0) items = arrayVals[0] as any[];
      }
      setCount(items.length);
    } catch (err) {
      setCount(0);
    }
  };

  useEffect(() => {
    readCartCount();
    const handler = (e: Event) => {
      const custom = e as CustomEvent<number>;
      if (typeof custom.detail === 'number') setCount(custom.detail);
      else readCartCount();
    };
    window.addEventListener('cartUpdated', handler as EventListener);
    return () => window.removeEventListener('cartUpdated', handler as EventListener);
  }, []);

  return (
      <Navbar bg="light" expand="lg" className="mb-3">
        <Container>
          <Navbar.Brand onClick={() => navigate('/')} style={{ cursor: 'pointer' }}>Neustack</Navbar.Brand>
          <Nav className="ms-auto">
            <Nav.Link onClick={() => navigate('/products')}>Products</Nav.Link>
            {!localStorage.getItem('userId') ? (
                <>
                  <Nav.Link onClick={() => navigate('/signin')}>Sign In</Nav.Link>
                  <Nav.Link onClick={() => navigate('/signup')}>Sign Up</Nav.Link>
                </>
            ) : (
                <>
                  <Nav.Link onClick={() => navigate('/cart')}>
                    Cart {count > 0 && <Badge bg="danger" pill style={{ marginLeft: 6 }}>{count}</Badge>}
                  </Nav.Link>
                  <Nav.Link onClick={() => navigate('/orders')}>Orders</Nav.Link> {/* Add Order List link */}
                  <Nav.Link onClick={() => {
                    // logout
                    localStorage.removeItem('userId');
                    localStorage.removeItem('username');
                    window.dispatchEvent(new CustomEvent('cartUpdated', { detail: 0 }));
                    navigate('/');
                  }}>Logout</Nav.Link>
                </>
            )}
          </Nav>
        </Container>
      </Navbar>
  );
};

export default Header;