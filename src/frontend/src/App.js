import React, {Component} from 'react';
import './App.css';
import {Layout, notification} from "antd";
import {Route, Switch, withRouter} from 'react-router-dom';
import NotFound from "./common/NotFound";
import LoadingIndicator from "./common/LoadingIndicator";
import {ACCESS_TOKEN} from "./constants/constants";
import AppHeader from "./common/AppHeader";
import Login from "./authentication/login/Login";
import Signup from "./user/signup/Signup";
import {getCurrentUser} from "./util/api/APIUtils";
import SiderMenu from "./common/SiderMenu/SiderMenu";
import PrivateRoute from "./common/PrivateRoute";
import MainPage from "./pages/MainPage";
import ManagePage from "./pages/manage/ManagePage";
import AccountSettings from "./user/edit/AccountSettings/AccountSettings";
import EventEditPage from "./events/Event/EventEditPage";
import CreateEventPage from "./pages/events/CreateEventPage";
import EventPage from "./pages/events/EventPage";
import CategoriesPage from "./pages/manage/CategoriesPage";
import UserProfile from "./pages/UserProfile/UserProfile";
import CreateCategoryPage from "./pages/category/CreateCategoryPage";
import ParticipatePage from "./pages/ParticipatePage/ParticipatePage";
import EditCategoryPage from "./pages/category/EditCategoryPage";

const {Content} = Layout;

class App extends Component {

  handleLogin = () => {
    notification.success({
      message: 'Bike Management',
      description: 'You\'re successfully logged in.',
    });
    this.loadCurrentUser();
    this.props.history.push("/");
  };
  handleLogout = (redirectTo, notificationType, description) => {
    localStorage.removeItem(ACCESS_TOKEN);

    this.setState({
      currentUser: null,
      isAuthenticated: false
    });
    this.props.history.push((redirectTo || "/"));

    notification[(notificationType || 'success')]({
      message: 'Bike Management',
      description: (description || 'You\'re successfully logged out.'),
    })
  };
  loadCurrentUser = () => {
    this.setState({isLoading: true});
    getCurrentUser()
      .then(response => {
        const authorities = response.data.grantedAuthorities.map(value => value.authority.substring(5))
        this.setState({
          currentUser: {...response.data, grantedAuthorities: authorities},
          isAuthenticated: true,
          isLoading: false
        })
      })
      .catch(error => this.setState({isLoading: false}))
  };

  constructor(props) {
    super(props);
    this.state = {
      currentUser: null,
      isAuthenticated: false,
      isLoading: false
    };
    notification.config({
      placement: 'topRight',
      top: 70,
      duration: 3,
    })
  }

  componentWillMount() {
    if (localStorage.getItem(ACCESS_TOKEN)) {
      this.loadCurrentUser()
    }
  }

  render() {
    if (this.state.isLoading) {
      return <LoadingIndicator/>
    }
    return (
      <Layout style={{minHeight: '100vh'}}>
        <SiderMenu currentUser={this.state.currentUser}/>
        <Layout>
          <AppHeader currentUser={this.state.currentUser}
                     onLogout={this.handleLogout}/>
          <Content className="app-content">
            <Switch>
              <PrivateRoute path="/account/profile"
                            component={UserProfile}
                            authenticated={this.state.currentUser}
                            currentUser={this.state.currentUser}
                            handleLogout={this.handleLogout}/>

              <Route path="/" exact component={MainPage}/>

              <PrivateRoute path="/events/new" exact
                            history={this.props.history}
                            component={CreateEventPage}
                            authenticated={this.state.currentUser
                            && (this.state.currentUser.grantedAuthorities.includes("ADMIN")
                              || this.state.currentUser.grantedAuthorities.includes("MANAGER"))}
                            currentUser={this.state.currentUser}/>

              <Route exact path="/events/participate/:id"
                // component={ParticipatePage}
                     render={(props) => (<ParticipatePage {...props} currentUser={this.state.currentUser}/>)}
                     authenticated={this.state.currentUser}
                     currentUser={this.state.currentUser}/>

              <Route exact path="/events/:id/edit"
                     component={EventEditPage}
                     authenticated={this.state.currentUser
                     && (this.state.currentUser.grantedAuthorities.includes("ADMIN")
                       || this.state.currentUser.grantedAuthorities.includes("MANAGER"))}/>


              <Route path="/events/:id"
                     render={(props) => (<EventPage {...props}
                                                    currentUser={this.state.currentUser}/>)}/>

              <PrivateRoute path="/events"
                            component={ManagePage}
                            authenticated={this.state.currentUser}
                            currentUser={this.state.currentUser}/>

              <PrivateRoute path="/categories/new"
                            component={CreateCategoryPage}
                            authenticated={this.state.currentUser
                            && this.state.currentUser.grantedAuthorities.includes("ADMIN")}
                            currentUser={this.state.currentUser}/>

              <PrivateRoute path="/categories/edit/:id"
                            authenticated={this.state.currentUser
                            && this.state.currentUser.grantedAuthorities.includes("ADMIN")}
                            currentUser={this.state.currentUser}
                            component={EditCategoryPage}/>

              <PrivateRoute path="/categories"
                            component={CategoriesPage}
                            authenticated={this.state.currentUser
                            && this.state.currentUser.grantedAuthorities.includes("ADMIN")}
                            currentUser={this.state.currentUser}/>

              <Route path="/login"
                     render={(props) => <Login onLogin={this.handleLogin} {...props}/>}/>

              <Route path="/signup" component={Signup}/>

              <PrivateRoute path="/users/account/settings"
                            authenticated={this.state.currentUser}
                            userId={this.state.currentUser ? this.state.currentUser.id : null}
                            handleLogout={this.handleLogout}
                            component={AccountSettings}/>

              <Route component={NotFound}/>
            </Switch>
          </Content>
        </Layout>
      </Layout>
    )
  }
}

export default withRouter(App);
