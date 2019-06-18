import React, {Component} from 'react'
import {
  EMAIL_MAX_LENGTH,
  EMAIL_REGEX,
  NAME_MAX_LENGTH,
  NAME_MIN_LENGTH,
  PHONE_NUMBER_REGEX
} from '../../../constants/constants'
import './EditProfile.css'
import {editUser, isEmailAvailable} from '../../../util/api/APIUtils'
import Avatar from '../Avatar/Avatar'
import {Button, Form, Input, notification} from 'antd'
import {withRouter} from "react-router-dom";

const FormItem = Form.Item;

class EditProfile extends Component {

  state = {
    firstName: {value: this.props.firstName, validateStatus: 'success'},
    lastName: {value: this.props.lastName, validateStatus: 'success'},
    email: {value: this.props.email, validateStatus: 'success'},
    phoneNumber: {value: this.props.phoneNumber, validateStatus: 'success'},
  };

  handleInputChange = (event, validationFun) => {
    const target = event.target;
    const inputName = target.name;
    const inputValue = target.value;

    this.setState({
      [inputName]: {
        value: inputValue,
        ...validationFun(inputValue)
      }
    })
  };

  handleSubmit = (event) => {
    event.preventDefault();

    const editRequest = {
      id: this.props.userId,
      firstName: this.state.firstName.value,
      lastName: this.state.lastName.value,
      email: this.state.email.value,
      phoneNumber: this.state.phoneNumber.value
    };

    editUser(editRequest)
      .then(response => {
        notification.success({
          message: 'Bike Management',
          description: 'Thank you! You\'re successfully edit your profile!',
        });
        this.props.history.push('/account/profile')
      }).catch(error => {
      notification.error({
        message: 'Bike Management',
        description: error.message || 'Sorry! Something went wrong. Please try again!'
      })
    })
  };

  isFormInvalid = () => {
    return !(
      this.state.firstName.validateStatus === 'success' &&
      this.state.lastName.validateStatus === 'success' &&
      this.state.phoneNumber.validateStatus === 'success' &&
      this.state.email.validateStatus === 'success' && (
        this.state.firstName.value !== this.props.firstName ||
        this.state.lastName.value !== this.props.lastName ||
        this.state.phoneNumber.value !== this.props.phoneNumber ||
        this.state.email.value !== this.props.email
      )
    )
  };


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
  };

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
  };

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
  };

  checkEmailAvailability = () => {
    // First check for client side errors in email
    const emailValue = this.state.email.value;
    const emailDefaultValue = this.props.email;
    const emailValidation = this.validateEmail(emailValue);

    if (emailValidation.validateStatus === 'error') {
      this.setState({
        email: {
          value: emailValue,
          ...emailValidation
        }
      });
      return
    }

    console.log(emailDefaultValue + " " + emailValue);
    if (emailDefaultValue === emailValue) {
      this.setState({
        email: {
          value: emailValue,
          validateStatus: 'success',
          errorMsg: null
        }
      });
      return
    }

    this.setState({
      email: {
        value: emailValue,
        validateStatus: 'validating',
        errorMsg: null
      }
    });


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
  };


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
  };


  render() {

    return (
      <div className="edit-container">
        <h1 className="page-title">Edit Profile</h1>
        <div className="edit-form">
          <Form onSubmit={this.handleSubmit}>
            <FormItem
              label="First Name"
              validateStatus={this.state.firstName.validateStatus}
              help={this.state.firstName.errorMsg}>
              <Input
                size="large"
                name="firstName"
                autoComplete="off"
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
                value={this.state.email.value}
                onBlur={this.checkEmailAvailability}
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
                value={this.state.phoneNumber.value}
                onChange={(event) => this.handleInputChange(event, this.validatePhoneNumber)}/>
            </FormItem>
            <FormItem>
              <Button
                type="primary"
                htmlType="submit"
                size="large"
                className="edit-form-button"
                disabled={this.isFormInvalid()}>Edit</Button>
            </FormItem>
          </Form>
        </div>

        <div className="avatar">
          <p>Avatar</p>
          <Avatar userId={this.props.userId}/>
        </div>
      </div>
    )
  }
}

export default withRouter(EditProfile);
