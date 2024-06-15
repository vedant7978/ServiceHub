import React, { useLayoutEffect, useRef, useState } from 'react';
import { Container, Nav, Navbar } from 'react-bootstrap';
import { NavLink } from 'react-router-dom';
import './NavBar.css';
import AppLogo from '../../assets/AppLogoBlack.png'
import { AppRoutes } from "../../utils/AppRoutes";

const NavBar = () => {
  const [activeTab, setActiveTab] = useState(getSelectedTab());
  const [underlineStyle, setUnderlineStyle] = useState({});
  const navRefs = useRef({});

  useLayoutEffect(() => {
    updateUnderline();
    window.addEventListener('resize', updateUnderline);
    return () => window.removeEventListener('resize', updateUnderline);
  }, [activeTab]);

  function getSelectedTab() {
    switch (window.location.pathname) {
      case AppRoutes.Dashboard:
        return 'Dashboard';
      case AppRoutes.ManageServices:
        return 'Manage Services';
      case AppRoutes.ManageContracts:
        return 'Manage Contracts';
      case AppRoutes.Wishlist:
        return 'Wishlist';
      case AppRoutes.Profile:
        return 'Profile';
      default:
        return 'Dashboard';
    }
  }

  const updateUnderline = () => {
    const activeNav = navRefs.current[activeTab];
    if (activeNav) {
      setUnderlineStyle({
        width: `${activeNav.offsetWidth}px`,
        left: `${activeNav.offsetLeft}px`
      });
    }
  };

  return (
    <Navbar expand="lg" className="top-nav-bar">
      <Container>
        <Navbar.Brand href={AppRoutes.Dashboard}>
          <img
            src={AppLogo}
            width="80"
            height="50"
            className="align-top object-fit-scale"
            alt="App Icon"
          />
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav"/>
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="nav-bar me-auto">
            <Nav.Link
              className="nav-link-custom"
              as={NavLink}
              to={AppRoutes.Dashboard}
              ref={(el) => (navRefs.current['Dashboard'] = el)}
              onClick={() => setActiveTab('Dashboard')}
              active={activeTab === 'Dashboard'}>
              Dashboard
            </Nav.Link>
            <Nav.Link
              className="nav-link-custom"
              as={NavLink}
              to={AppRoutes.ManageServices}
              ref={(el) => (navRefs.current['Manage Service'] = el)}
              onClick={() => setActiveTab('Manage Service')}
              active={activeTab === 'Manage Service'}>
              Manage Service
            </Nav.Link>
            <Nav.Link
              className="nav-link-custom"
              as={NavLink}
              to={AppRoutes.ManageContracts}
              ref={(el) => (navRefs.current['Manage Contracts'] = el)}
              onClick={() => setActiveTab('Manage Contracts')}
              active={activeTab === 'Manage Contracts'}>
              Manage Contracts
            </Nav.Link>
            <Nav.Link
              className="nav-link-custom"
              as={NavLink}
              to={AppRoutes.Wishlist}
              ref={(el) => (navRefs.current['Wishlist'] = el)}
              onClick={() => setActiveTab('Wishlist')}
              active={activeTab === 'Wishlist'}>
              Wishlist
            </Nav.Link>
            <Nav.Link
              className="nav-link-custom"
              as={NavLink}
              to={AppRoutes.Profile}
              ref={(el) => (navRefs.current['Profile'] = el)}
              onClick={() => setActiveTab('Profile')}
              active={activeTab === 'Profile'}>
              Profile
            </Nav.Link>
          </Nav>
          <Nav>
            {/* [TODO]: Update this for sign out functionality (might user Modal of Bootstrap to show dialog)*/}
            Sign Out
          </Nav>
        </Navbar.Collapse>
      </Container>
      <div className="underline" style={underlineStyle}/>
    </Navbar>
  );
};

export default NavBar;
