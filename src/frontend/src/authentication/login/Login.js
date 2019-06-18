import React, {Component} from 'react';
import {Link} from 'react-router-dom';
import {ACCESS_TOKEN} from '../../constants/constants';
import "./Login.css";
import {Button, Form, Icon, Input, notification} from 'antd';
import {loginUser} from "../../util/api/APIUtils";

const FormItem = Form.Item;

class Login extends Component {
  render() {
    const AntWrappedLoginForm = Form.create()(LoginForm);
    return (
        <div className="login-container">
          <h1>Login</h1>
          <div>
            <AntWrappedLoginForm onLogin={this.props.onLogin}/>
          </div>
        </div>
    );
  }
}

class LoginForm extends Component {

  handleSubmit = (event) => {
    event.preventDefault();
    this.props.form.validateFields((err, values) => {
      if (!err) {
        const loginRequest = {...values};

        loginUser(loginRequest)
          .then(response => {
            const data = response.data;
            if (data.accessToken) {
              localStorage.setItem(ACCESS_TOKEN, data.accessToken);
              this.props.onLogin();
            } else {
              notification.error({
                message: 'Bike Management',
                description: 'Sorry! Something went wrong. Please try again!'
              });
            }
          }).catch(error => {
          console.log(error);
          if (error.status === 401) {
            notification.error({
              message: 'Bike Management',
              description: 'Your Username or Password is incorrect. Please try again!'
            });
          } else {
            notification.error({
              message: 'Bike Management',
              description: error.message || 'Sorry! Something went wrong. Please try again!'
            });
          }
        });
      }
    });
  };

  render() {
    const {getFieldDecorator} = this.props.form;
    return (
        <Form onSubmit={this.handleSubmit}>
          <FormItem>
            {getFieldDecorator('email', {
              rules: [{required: true, message: 'Please input your email!'}],
            })(
                <Input
                    prefix={<Icon type="user"/>}
                    size="large"
                    name="email"
                    placeholder="Email"/>
            )}
          </FormItem>
          <FormItem>
            {getFieldDecorator('password', {
              rules: [{required: true, message: 'Please input your Password!'}],
            })(
                <Input
                    prefix={<Icon type="lock"/>}
                    size="large"
                    name="password"
                    type="password"
                    placeholder="Password"/>
            )}
          </FormItem>
          <FormItem>
            <Button type="primary" htmlType="submit" size="large" className="login-form-button">Login</Button>
            Or <Link to="/signup">register now!</Link>
          </FormItem>
        </Form>
    );
  }
}

export default Login;