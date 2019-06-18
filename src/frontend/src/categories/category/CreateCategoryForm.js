import React, {Component} from 'react';
import {NAME_MAX_LENGTH, NAME_MIN_LENGTH} from "../../constants/constants";
import {Button, Form, Input} from 'antd'

const FormItem = Form.Item;

class CreateCategoryForm extends Component {
  constructor(props) {
    super();
    this.state = {
      category: {
        id: null,
        name: null,
      },
      action: props.action,
      validations: {
        name: {},
      },
    };

    if (props.category != null) {
      this.state.category = props.category;
    }
  }

  validateName = (name) => {
    if (name.length < NAME_MIN_LENGTH) {
      return {
        validationStatus: 'error',
        errorMsg: `Name is too short (Minimum ${NAME_MIN_LENGTH} characters needed.)`
      }
    }
    if (name.length > NAME_MAX_LENGTH) {
      return {
        validationStatus: 'error',
        errorMsg: `Name is too long (Maximum ${NAME_MAX_LENGTH} characters allowed.)`
      }
    }
    return {
      validationStatus: 'success',
      errorMsg: null
    }
  };


  handleInputChange = (event, validationFunc) => {
    const target = event.target;
    const inputName = target.name;
    const inputValue = target.value;

    this.setState({
      category: {
        ...this.state.category,
        [inputName]: inputValue
      },
      validations: {
        ...this.state.validations,
        [inputName]: {...validationFunc(inputValue)},
      },
    })
  };

  isValidForm = () => {
    return this.state.validations.name.validationStatus === 'success';
  };

  handleSubmit = (ev) => {
    ev.preventDefault();
    const categoryRequest = {...this.state.category};
    this.state.action(categoryRequest);
  };

  render() {
    return (
      <div>
        <Form onSubmit={this.handleSubmit} className="signup-form">
          <FormItem
            label="Name"
            validateStatus={this.state.validations.name.validateStatus}
            help={this.state.validations.name.errorMsg}>
            <Input
              size="large"
              name="name"
              autoComplete="off"
              placeholder="Enter category name"
              value={this.state.category.name}
              onChange={(ev) => this.handleInputChange(ev, this.validateName)}/>
          </FormItem>
          <FormItem>
            <Button
              type="primary"
              htmlType="submit"
              size="large"
              disabled={!this.isValidForm()}>Create</Button>
          </FormItem>
        </Form>
      </div>
    );
  }
}

export default CreateCategoryForm;