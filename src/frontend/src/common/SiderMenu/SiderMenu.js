import React, {Component} from 'react';
import './SiderMenu.css';
import {Icon, Layout, Menu} from 'antd';
import {Link, withRouter} from "react-router-dom";
import logo from '../../assets/logo.png'

const {Sider} = Layout;
const SubMenu = Menu.SubMenu;

class SiderMenu extends Component {

  state = {
    collapsed: false,
  };

  onCollapse = (collapsed) => {
    this.setState({collapsed});
  };

  render() {

    let menuItems = [];
    let menuItemsManager = [];
    let menuItemsAdmin = [];
    const isAdmin = this.props.currentUser ? this.props.currentUser.grantedAuthorities.includes("ADMIN") : false;
    const isManager = this.props.currentUser ? this.props.currentUser.grantedAuthorities.includes("MANAGER") : false;


    if (this.props.currentUser) {

      if (isAdmin) {
        menuItemsAdmin.push(
          <Menu.Item key="championships">
            <Link to='/championships'>
              <span>Championships</span>
            </Link>
          </Menu.Item>);

        menuItemsAdmin.push(
          <Menu.Item key="categories">
            <Link to='/categories'>
              <span>Categories</span>
            </Link>
          </Menu.Item>);
      }

      if (isManager) {
        menuItemsManager.push(
          <Menu.Item key="events">
            <Link to='/events'>
              <span>Events</span>
            </Link>
          </Menu.Item>);
      }

      if (isAdmin || isManager) {
        menuItems.push(
          <SubMenu key="manage"
                   title={<span><Icon type="compass"/><span>Manage</span></span>}>
            {menuItemsAdmin}
            {menuItemsManager}
          </SubMenu>);
      }
    }

    if (this.props.currentUser) {
      menuItems.push(
        <SubMenu
          key="user"
          title={<span><Icon type="user"/><span>User</span></span>}>
          <Menu.Item key="profile">
            <Link to='/account/profile'>Profile</Link>
          </Menu.Item>
          <Menu.Item key="/users/account/settings">
            <Link to={`/users/account/settings`}>Edit</Link>
          </Menu.Item>
        </SubMenu>
      );
    }

    return (
      <Sider
        collapsible
        collapsed={this.state.collapsed}
        onCollapse={this.onCollapse}>
        <div className='logo'>
          <Link to="/">
            <img src={logo} alt="logo"/>
            <h1>BCMS</h1>
          </Link>
        </div>
        <Menu theme="dark" defaultSelectedKeys={[this.props.location.pathname]} mode="inline">
          <Menu.Item key="/">
            <Link to="/">
              <Icon type="home"/>
              <span>Home</span>
            </Link>
          </Menu.Item>
          {menuItems}
        </Menu>
      </Sider>
    );
  }
}

export default withRouter(SiderMenu);