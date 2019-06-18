import React, {Component} from 'react'
import {Link} from 'react-router-dom'
import {
  EMAIL_MAX_LENGTH,
  EMAIL_REGEX,
  NAME_MAX_LENGTH,
  NAME_MIN_LENGTH,
  PASSWORD_MAX_LENGTH,
  PASSWORD_MIN_LENGTH,
  PHONE_NUMBER_REGEX
} from '../../constants/constants'
import './Signup.css'
import {isEmailAvailable, signUpUser} from '../../util/api/APIUtils'

import {Button, Form, Input, notification} from 'antd'

const FormItem = Form.Item

class Signup extends Component {

  state = {
    firstName: {value: ''},
    lastName: {value: ''},
    email: {value: ''},
    phoneNumber: {value: ''},
    password: {value: ''},
    matchingPassword: {value: ''}
  }

  handleInputChange = (event, validationFun) => {
    const target = event.target
    const inputName = target.name
    const inputValue = target.value

    this.setState({
      [inputName]: {
        value: inputValue,
        ...validationFun(inputValue)
      }
    })
  }

  handleSubmit = (event) => {
    event.preventDefault()

    const signupRequest = {
      firstName: this.state.firstName.value,
      lastName: this.state.lastName.value,
      email: this.state.email.value,
      phoneNumber: this.state.phoneNumber.value,
      password: this.state.password.value,
      matchingPassword: this.state.matchingPassword.value
    }

    signUpUser(signupRequest)
      .then(response => {
        notification.success({
          message: 'Bike Management',
          description: 'Thank you! You\'re successfully registered. Please Login to continue!',
        })
        this.props.history.push('/login')
      }).catch(error => {
      notification.error({
        message: 'Bike Management',
        description: error.message || 'Sorry! Something went wrong. Please try again!'
      })
    })
  }

  isFormInvalid = () => {
    return !(
      this.state.firstName.validateStatus === 'success' &&
      this.state.lastName.validateStatus === 'success' &&
      this.state.phoneNumber.validateStatus === 'success' &&
      this.state.email.validateStatus === 'success' &&
      this.state.password.validateStatus === 'success' &&
      this.state.matchingPassword.validateStatus === 'success'
    )
  }
  validateFirstName = (firstName) => {
    if (firstName.length < NAME_MIN_LENGTH) {
      return {
        validateStatus: 'error',
        errorMsg: `Name is too short (Minimum ${NAME_MIN_LENGTH} characters needed.)`
      }
    } else if (firstName.length > NAME_MAX_LENGTH) {
      return {
        validateStatus: 'error',
        errorMsg: `Name is too long (Maximum ${NAME_MAX_LENGTH} characters allowed.)`
      }
    } else {
      return {
        validateStatus: 'success',
        errorMsg: null,
      }
    }
  }

  validateLastName = (lastName) => {
    if (lastName.length < NAME_MIN_LENGTH) {
      return {
        validateStatus: 'error',
        errorMsg: `Name is too short (Minimum ${NAME_MIN_LENGTH} characters needed.)`
      }
    } else if (lastName.length > NAME_MAX_LENGTH) {
      return {
        validateStatus: 'error',
        errorMsg: `Name is too long (Maximum ${NAME_MAX_LENGTH} characters allowed.)`
      }
    } else {
      return {
        validateStatus: 'success',
        errorMsg: null,
      }
    }
  }

  validateEmail = (email) => {
    if (!email) {
      return {
        validateStatus: 'error',
        errorMsg: 'Email may not be empty'
      }
    }

    if (!EMAIL_REGEX.test(email)) {
      return {
        validateStatus: 'error',
        errorMsg: 'Email not valid'
      }
    }

    if (email.length > EMAIL_MAX_LENGTH) {
      return {
        validateStatus: 'error',
        errorMsg: `Email is too long (Maximum ${EMAIL_MAX_LENGTH} characters allowed)`
      }
    }

    return {
      validateStatus: 'success',
      errorMsg: null
    }
  }

  validateEmailAvailability = () => {
    // First check for client side errors in email
    const emailValue = this.state.email.value
    const emailValidation = this.validateEmail(emailValue)

    if (emailValidation.validateStatus === 'error') {
      this.setState({
        email: {
          value: emailValue,
          ...emailValidation
        }
      })
      return
    }

    this.setState({
      email: {
        value: emailValue,
        validateStatus: 'validating',
        errorMsg: null
      }
    })

    isEmailAvailable(emailValue)
      .then(response => {
        if (response.data.available) {
          this.setState({
            email: {
              value: emailValue,
              validateStatus: 'success',
              errorMsg: null
            }
          })
        } else {
          this.setState({
            email: {
              value: emailValue,
              validateStatus: 'error',
              errorMsg: 'This Email is already registered'
            }
          })
        }
      }).catch(error => {
      // Marking validateStatus as success, Form will be rechecked at server
      this.setState({
        email: {
          value: emailValue,
          validateStatus: 'success',
          errorMsg: null
        }
      })
    })
  }


  validatePhoneNumber = (phoneNumber) => {
    if (!phoneNumber) {
      return {
        validateStatus: 'error',
        errorMsg: 'Phone number may not be empty'
      }
    }

    if (!PHONE_NUMBER_REGEX.test(phoneNumber)) {
      return {
        validateStatus: 'error',
        errorMsg: 'Phone number not valid'
      }
    }

    return {
      validateStatus: 'success',
      errorMsg: null
    }
  }

  validatePassword = (password) => {
    if (password.length < PASSWORD_MIN_LENGTH) {
      return {
        validateStatus: 'error',
        errorMsg: `Password is too short (Minimum ${PASSWORD_MIN_LENGTH} characters needed.)`
      }
    } else if (password.length > PASSWORD_MAX_LENGTH) {
      return {
        validateStatus: 'error',
        errorMsg: `Password is too long (Maximum ${PASSWORD_MAX_LENGTH} characters allowed.)`
      }
    } else {
      return {
        validateStatus: 'success',
        errorMsg: null,
      }
    }
  }

  validateMatchingPassword = (matchingPassword) => {
    if (matchingPassword !== this.state.password.value) {
      return {
        validateStatus: 'error',
        errorMsg: `This field doesn't match the previous one`
      }
    } else {
      return {
        validateStatus: 'success',
        errorMsg: null,
      }
    }
  }

  render () {
    return (
      <div className="signup-container">
        <h1 className="page-title">Sign Up</h1>
        <div className="signup-content">
          <Form onSubmit={this.handleSubmit} className="signup-form">
            <FormItem
              label="First Name"
              validateStatus={this.state.firstName.validateStatus}
              help={this.state.firstName.errorMsg}>
              <Input
                size="large"
                name="firstName"
                autoComplete="off"
                placeholder="Your first name"
                value={this.state.firstName.value}
                onChange={(event) => this.handleInputChange(event, this.validateFirstName)}/>
            </FormItem>
            <FormItem
              label="Last Name"
              validateStatus={this.state.lastName.validateStatus}
              help={this.state.lastName.errorMsg}>
              <Input
                size="large"
                name="lastName"
                autoComplete="off"
                placeholder="Your last name"
                value={this.state.lastName.value}
                onChange={(event) => this.handleInputChange(event, this.validateLastName)}/>
            </FormItem>
            <FormItem
              label="Email"
              hasFeedback
              validateStatus={this.state.email.validateStatus}
              help={this.state.email.errorMsg}>
              <Input
                size="large"
                name="email"
                type="email"
                autoComplete="off"
                placeholder="Your email"
                value={this.state.email.value}
                onBlur={this.validateEmailAvailability}
                onChange={(event) => this.handleInputChange(event, this.validateEmail)}/>
            </FormItem>
            <FormItem
              label="Phone number"
              validateStatus={this.state.phoneNumber.validateStatus}
              help={this.state.phoneNumber.errorMsg}>
              <Input
                size="large"
                name="phoneNumber"
                autoComplete="off"
                placeholder="Your phone number"
                value={this.state.phoneNumber.value}
                onChange={(event) => this.handleInputChange(event, this.validatePhoneNumber)}/>
            </FormItem>
            <FormItem
              label="Password"
              validateStatus={this.state.password.validateStatus}
              help={this.state.password.errorMsg}>
              <Input
                size="large"
                name="password"
                type="password"
                autoComplete="off"
                placeholder="A password between 6 to 20 characters"
                value={this.state.password.value}
                onChange={(event) => this.handleInputChange(event, this.validatePassword)}/>
            </FormItem>
            <FormItem
              label="Confirm password"
              validateStatus={this.state.matchingPassword.validateStatus}
              help={this.state.matchingPassword.errorMsg}>
              <Input
                size="large"
                name="matchingPassword"
                type="password"
                autoComplete="off"
                placeholder="Password again"
                value={this.state.matchingPassword.value}
                onChange={(event) => this.handleInputChange(event, this.validateMatchingPassword)}/>
            </FormItem>
            <FormItem>
              <Button
                type="primary"
                htmlType="submit"
                size="large"
                className="signup-form-button"
                disabled={this.isFormInvalid()}>Sign up</Button>
              Already registered? <Link to="/login">Login now!</Link>
            </FormItem>
          </Form>
        </div>
      </div>
    )
  }

}

export default Signup
