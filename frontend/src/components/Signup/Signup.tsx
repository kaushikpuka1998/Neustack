import { type FC, useState, useEffect } from 'react';
import { Container, Form, Button, Alert, InputGroup, Spinner } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { api } from '../../config';

const Signup: FC = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [email, setEmail] = useState('');
  const [usernameAvailable, setUsernameAvailable] = useState<boolean | null>(null);
  const [checkingUsername, setCheckingUsername] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!username.trim()) return setError('Please enter username');
    if (!password) return setError('Please enter a password');
    setLoading(true);
    setError(null);
    try {
      // try common signup endpoints
      let res;
      try {
        res = await api.post('/users/register', { name: username, password, email });
      } catch (err) {
        // fallback to /users/add which now supports password
        res = await api.post('/users/add', { name: username, password, email });
      }

      if (!res.data?.success) {
        setError(res.data?.message || 'Signup failed');
      } else {
        const id = res.data?.user?.id || res.data?.id || res.data?.userId || res.data?.data?.id;
        if (id) localStorage.setItem('userId', String(id));
        localStorage.setItem('username', username);
        setSuccess(true);
        setUsername(''); setPassword(''); setEmail('');
        setTimeout(() => navigate('/products'), 600);
      }
    } catch (err: any) {
      setError(err?.response?.data?.message || err.message || 'Network error');
    } finally {
      setLoading(false);
    }
  };

  // Check username availability with debounce
  useEffect(() => {
    setUsernameAvailable(null);
    if (!username || username.trim().length < 3) {
      setCheckingUsername(false);
      return;
    }

    let mounted = true;
    setCheckingUsername(true);
    const timer = setTimeout(async () => {
      try {
        const current = username;
        const res = await api.post('/users/check-username', { name: current });
        if (!mounted) return;
        // only set if username hasn't changed
        if(res.data.message == "Username available"){
            setUsernameAvailable(true);
        }
        if(res.data.message == "Username already taken"){
            setUsernameAvailable(false);
        }
        setCheckingUsername(true);
      } catch (err) {
          if(mounted) setCheckingUsername(false);
        // ignore errors for availability check but reset state
      } finally {
        if (mounted) setCheckingUsername(false);
      }
    }, 400);

    return () => {
      mounted = false;
      clearTimeout(timer);
      setCheckingUsername(false);
    };
  }, [username]);

  return (
    <Container className="d-flex justify-content-center align-items-center" style={{ minHeight: '60vh' }}>
      <div style={{ width: '100%', maxWidth: 480 }}>
        <Form onSubmit={handleSubmit} className="p-4 bg-light rounded shadow-sm">
          <h3 className="mb-3 text-center">Sign Up</h3>

          {success && <Alert variant="success">Account created.</Alert>}
          {error && <Alert variant="danger">{error}</Alert>}

          <Form.Group className="mb-3">
            <Form.Label>Username</Form.Label>
            <InputGroup>
              <InputGroup.Text>👤</InputGroup.Text>
              <Form.Control
                value={username}
                onChange={e => setUsername(e.target.value)}
                placeholder="Choose a username"
                required
                aria-describedby="username-help"
                isInvalid={usernameAvailable === false}
              />
            </InputGroup>
            <Form.Text id="username-help">
              {checkingUsername && (
                <span className="text-muted">Checking availability... </span>
              )}
              {usernameAvailable === true && !checkingUsername && (
                <span className="text-success">Username is available ✓</span>
              )}
              {usernameAvailable === false && !checkingUsername && (
                <span className="text-danger">Username is already taken ✕</span>
              )}
            </Form.Text>
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Password</Form.Label>
            <Form.Control value={password} onChange={e => setPassword(e.target.value)} type="password" placeholder="Password" />
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Email (optional)</Form.Label>
            <Form.Control value={email} onChange={e => setEmail(e.target.value)} type="email" placeholder="name@example.com" />
          </Form.Group>

          <Button type="submit" variant="primary" className="w-100" size="lg" disabled={loading || checkingUsername || usernameAvailable === false}>
            {loading ? <><Spinner animation="border" size="sm" /> <span style={{marginLeft:8}}>Creating...</span></> : 'Create Account'}
          </Button>
        </Form>
      </div>
    </Container>
  );
};

export default Signup;

