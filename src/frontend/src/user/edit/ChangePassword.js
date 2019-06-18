import React, {Component} from 'react'
import {PASSWORD_MAX_LENGTH, PASSWORD_MIN_LENGTH,} from '../../constants/constants'
import './EditProfile/EditProfile.css'
import {Button, Form, Input, notification} from 'antd'
import {changePassword} from "../../util/api/APIUtils";

const FormItem = Form.Item

class ChangePassword extends Component {

  state = {
    oldPassword: {value: ''},
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

    const changePasswordRequest = {
      id: this.props.userId,
      oldPassword: this.state.oldPassword.value,
      password: this.state.password.value,
      matchingPassword: this.state.matchingPassword.value
    }
    changePassword(changePasswordRequest)
      .then(response => {
        notification.success({
          message: 'Bike Management',
          description: 'Thank you! You\'re successfully change your password!',
        })
        //  this.props.history.push('/')
        this.setState({
          oldPassword: {value: ''},
          password: {value: ''},
          matchingPassword: {value: ''}
        });
      }).catch(error => {
      notification.error({
        message: 'Bike Management',
        description: error.message || 'Sorry! Something went wrong. Please try again!'
      })
    })
    this.setState({
      oldPassword: {value: ''},
      password: {value: ''},
      matchingPassword: {value: ''}
    });
  }

  isFormInvalid = () => {
    return !(
      this.state.password.validateStatus === 'success' &&
      this.state.matchingPassword.validateStatus === 'success' &&
      this.state.password.value === this.state.matchingPassword.value
  )
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

  validateOldPassword = (oldPassword) => {
    return {
      validateStatus: 'success',
      errorMsg: null,
    }
  }


  render() {
    return (
      <div className="edit-container">
        <h1 className="page-title">Change Password</h1>
        <div className="edit-form">
          <Form onSubmit={this.handleSubmit}>
            <FormItem
              label="Old password">
              <Input
                size="large"
                name="oldPassword"
                type="password"
                autoComplete="off"
                placeholder="A password between 6 to 20 characters"
                value={this.state.oldPassword.value}
                onChange={(event) => this.handleInputChange(event, this.validateOldPassword)}/>
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
                className="edit-form-button"
                disabled={this.isFormInvalid()}>Change Password</Button>
            </FormItem>
          </Form>
        </div>
      </div>
    )
  }

}

export default ChangePassword