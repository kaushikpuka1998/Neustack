import { type FC, useState } from "react";
import { Container, Form, Button, Alert, InputGroup, Spinner } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { api } from '../../config';

import reactLogo from '../../assets/react.svg'
import viteLogo from '../../assets/vite.svg'
import heroImg from '../../assets/hero.png'

const SignIn: FC = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!username.trim()) {
      setError('Please enter a username');
      return;
    }
    if (!password) {
      setError('Please enter your password');
      return;
    }

    setLoading(true);
    setError(null);
    setSuccess(false);

    try {
      // Try login endpoint first; if not available fallback to users/add supporting password
      let response;
      try {
        // backend LoginRequest expects { username, password }
        response = await api.post('/users/login', { username: username, password });
      } catch (err) {
        setSuccess(false);
      }

      if (!response?.data?.success) {
        setError(response?.data?.message || 'Unable to sign in');
      } else {
        // extract user object from common response shapes
        const user = response.data?.data || response.data?.user || response.data;
        if (user) {
          if (user.password) delete user.password;
          if (user.id) localStorage.setItem('userId', String(user.id));
          localStorage.setItem('username', user.name || username);
          localStorage.setItem('user', user.name);
        }

        setSuccess(true);
        setUsername('');
        setPassword('');
        // short delay so user can see success alert, then navigate
        setTimeout(() => navigate('/products'), 600);
      }
    } catch (err: any) {
      setError(err?.response?.data?.message || err.message || 'Network error');
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <div className="hero">
        <img src={heroImg} className="base" width="170" height="179" alt="" />
        <img src={reactLogo} className="framework" alt="React logo" />
        <img src={viteLogo} className="vite" alt="Vite logo" />
      </div>

      <Container className="d-flex justify-content-center align-items-center" style={{ minHeight: '35vh' }}>
        <div style={{ width: '100%', maxWidth: '480px' }}>
          <Form onSubmit={handleSubmit} className="p-4 bg-light rounded shadow-sm" noValidate>
            {success && (
              <Alert variant="success" onClose={() => setSuccess(false)} dismissible>
                Successfully signed in.
              </Alert>
            )}

            {error && (
              <Alert variant="danger" onClose={() => setError(null)} dismissible>
                {error}
              </Alert>
            )}

            <h3 className="mb-3 text-center text-primary">Sign In</h3>

            <Form.Group controlId="username" className="mb-3">
              <Form.Label>Username</Form.Label>
              <InputGroup>
                <InputGroup.Text id="username-addon">👤</InputGroup.Text>
                <Form.Control
                  type="text"
                  placeholder="Enter your username"
                  aria-describedby="username-addon"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  isInvalid={!!error}
                  disabled={loading}
                  required
                />
              </InputGroup>

              <Form.Control.Feedback type="invalid">{error}</Form.Control.Feedback>
            </Form.Group>

            <Form.Group controlId="password" className="mb-3">
              <Form.Label>Password</Form.Label>
              <InputGroup>
                <InputGroup.Text id="password-addon">🔒</InputGroup.Text>
                <Form.Control
                  type="password"
                  placeholder="Enter your password"
                  aria-describedby="password-addon"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  isInvalid={!!error}
                  disabled={loading}
                  required
                />
              </InputGroup>
            </Form.Group>

            <Button variant="primary" type="submit" className="w-100" size="lg" disabled={loading || !username.trim() || !password}>
              {loading ? (
                <>
                  <Spinner animation="border" size="sm" role="status" aria-hidden="true" />
                  <span style={{ marginLeft: 8 }}>Submitting…</span>
                </>
              ) : (
                'Submit'
              )}
            </Button>
          </Form>
        </div>
      </Container>
    </>
  );
};

export default SignIn;