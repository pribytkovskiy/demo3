import React, {Component} from 'react';
import {Button, DatePicker, Form, Input, Select} from 'antd';
import Moment from 'moment';
import {EVENT_NAME_MAX_LENGTH, TEXT_AREA_DEFAULT_MAX_LENGTH} from "../../constants/constants";

const FormItem = Form.Item;
const Option = Select.Option;
const TextArea = Input.TextArea;

class EventForm extends Component {
  state = {
    event: {
      id: null,
      name: null,
      dateTime: null,
      description: null,
      managerId: null,
      categories: [],
    },

    validations: {
      name: {},
      description: {},
      dateTime: {},
      categories: {},
    },

    action: null,
    categories: [],
    currentUser: null,
  };

  componentWillMount() {
    this.setState({
      currentUser: this.props.currentUser,
      categories: this.props.categories,
      action: this.props.action,
      event: {
        ...this.state.event,
        dateTime: Moment().add(1, 'day'),
      }
    });
  }

  handleOptionsChange = (categories, keySet) => {
    if (categories.length < 1) {
      this.setState({
        event: {
          ...this.state.event,
          categories: [],
        },
        validations: {
          ...this.state.validations,
          categories: {
            validationStatus: 'error',
            errorMsg: `Add at least ${1} category`
          },
        },
      });
    } else {
      this.setState({
        event: {
          ...this.state.event,
          categories: [...Array(categories.length)]
            .map((category, index) => ({id: keySet[index].key, name: categories[index]})),
        },
        validations: {
          ...this.state.validations,
          categories: {
            validationStatus: 'success',
            errorMsg: null,
          },
        },
      });
    }
  };

  handleDateChange = (newDateTime) => {
    if (newDateTime < Moment()) {
      this.setState({
        validations: {
          ...this.state.validations,
          dateTime: {
            validationStatus: 'error',
            errorMsg: 'Date is not valid',
          }
        }
      });
    } else {
      this.setState({
        event: {
          ...this.state.event,
          dateTime: newDateTime,
        },
        validations: {
          ...this.state.validations,
          dateTime: {
            validationStatus: 'success',
            errorMsg: null,
          }
        }
      });
    }
  };

  handleInputChange = (event, validationFunc) => {
    const target = event.target;
    const inputName = target.name;
    const inputValue = target.value;

    this.setState({
      event: {
        ...this.state.event,
        [inputName]: inputValue
      },
      validations: {
        ...this.state.validations,
        [inputName]: {...validationFunc(inputValue)},
      },
    })
  };

  validateName = (name) => {
    if (name.length < 1) {
      return {
        validationStatus: 'error',
        errorMsg: `Name is too short (Minimum ${1} characters needed.)`
      }
    } else if (name.length > EVENT_NAME_MAX_LENGTH) {
      return {
        validationStatus: 'error',
        errorMsg: `Name is too long (Maximum ${EVENT_NAME_MAX_LENGTH} characters allowed.)`
      }
    } else {
      return {
        validationStatus: 'success',
        errorMsg: null,
      }
    }
  };

  validateDescription = (name) => {
    if (name.length < 1) {
      return {
        validationStatus: 'error',
        errorMsg: `Description is too short (Minimum ${1} characters needed.)`
      }
    }
    if (name.length > TEXT_AREA_DEFAULT_MAX_LENGTH) {
      return {
        validationStatus: 'error',
        errorMsg: `Description is too long (Maximum ${TEXT_AREA_DEFAULT_MAX_LENGTH} characters allowed.)`
      }
    }
    return {
      validationStatus: 'success',
      errorMsg: null
    }
  };

  isValidForm = () => {
    return (
      this.state.validations.name.validationStatus === 'success' &&
      this.state.validations.description.validationStatus === 'success' &&
      this.state.validations.dateTime.validationStatus === 'success' &&
      this.state.validations.categories.validationStatus === 'success'
    )
  };

  handleSubmit = (e) => {
    e.preventDefault();
    const eventRequest = {...this.state.event};
    eventRequest.dateTime = eventRequest.dateTime.format('YYYY-MM-DDTHH:mm:ss');
    eventRequest.managerId = this.state.currentUser.id;
    this.state.action(eventRequest);
  };

  render() {
    return (
      <Form onSubmit={this.handleSubmit}>
        <FormItem
          label="Event Name"
          validateStatus={this.state.validations.name.validationStatus}
          help={this.state.validations.name.errorMsg}>
          <Input
            name="name"
            autoComplete="off"
            placeholder="Name of new event"
            value={this.state.event.name}
            onChange={(event) => this.handleInputChange(event, this.validateName)}/>
        </FormItem>
        <FormItem
          label="Event Description"
          validateStatus={this.state.validations.description.validationStatus}
          help={this.state.validations.description.errorMsg}>
          <TextArea
            name="description"
            autoComplete="off"
            placeholder="Description of new event"
            rows={6}
            value={this.state.event.description}
            onChange={(event) => this.handleInputChange(event, this.validateDescription)}/>
        </FormItem>
        <FormItem
          hasFeedback
          label="Categories"
          validateStatus={this.state.validations.categories.validationStatus}
          help={this.state.validations.categories.errorMsg}>
          <Select
            mode="multiple"
            placeholder="Select categories"
            onChange={(categories, i) => this.handleOptionsChange(categories, i)}>
            {
              this.state.categories.map(category => (
                <Option key={category.id} value={category.name}>{category.name}</Option>))
            }
          </Select>
        </FormItem>
        <FormItem
          hasFeedback
          label="Date"
          validateStatus={this.state.validations.dateTime.validateStatus}
          help={this.state.validations.dateTime.errorMsg}>
          <DatePicker
            showTime
            style={{width: '100%'}}
            format="MMMM Do YYYY, h:mm"
            allowClear={false}
            value={this.state.event.dateTime}
            onChange={(dt) => this.handleDateChange(dt)}/>
        </FormItem>
        <FormItem>
          <Button
            type="primary"
            htmlType="submit"
            disabled={!this.isValidForm()}>Create</Button>
        </FormItem>
      </Form>
    )
  }
}

export default EventForm;
