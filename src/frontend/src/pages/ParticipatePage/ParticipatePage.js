import React, {Component} from 'react';
import {getEvent, getParticipantByEventId, getUserProfile, participateInEvent} from "../../util/api/APIUtils";
import {Button, Card, Form, Input, notification, Select, Skeleton} from 'antd';
import {NAME_MAX_LENGTH, NAME_MIN_LENGTH} from "../../constants/constants";
import './ParticipatePage.css';

const FormItem = Form.Item;

class ParticipatePage extends Component {

  constructor(props) {
    super();
    this.state = {
      event: {
        name: null,
        categories: [],
      },
      eventId: props.match.params.id,
      categoryOptions: [],
      formLoading: true,
      currentUser: props.currentUser,
      categoriesInEvent: [],
      validations: {
        firstName: {validateStatus: 'error'},
        lastName: {validateStatus: 'error'},
        category: {validateStatus: 'error'},
        competitionNumber: {validateStatus: 'error'},
      },
      participant: {
        userId: null,
        firstName: null,
        lastName: null,
        competitionNumber: null,
        categoryId: null,
        eventId: props.match.params.id,
      },
      userProfile: {
        firstName: null,
        lastName: null,
      },
      notAvailableNumbers: [],
    };
  }

  componentDidMount() {
    getEvent(this.state.eventId)
      .then(response => this.setState({
        event: response.data,
        formLoading: false,
        categoriesInEvent: Object.keys(response.data.categories).map((objectKey) => {
          return [...Array(response.data.categories[objectKey]).map(c => c.name)]
        }).join(',')
      }))
      .catch(reason => {
        notification.error({
          message: 'Bike Management',
          description: 'Error receiving available categories!',
        });
        console.log(reason);
      });

    getParticipantByEventId(this.state.eventId)
      .then(
        response => this.setState({notAvailableNumbers: response.data.map(participant => participant.competitionNumber).join(", ")})
      )
      .catch(reason => {
        notification.error({
          message: 'Bike Management',
          description: 'Error receiving competition numbers!',
        });
        console.log(reason);
      });

    if (this.state.currentUser) {
      getUserProfile(this.state.currentUser.id)
        .then(response => this.setState({
            userProfile: response.data,
            participant: {
              ...this.state.participant,
              firstName: response.data.firstName,
              lastName: response.data.lastName,
              userId: this.state.currentUser.id,
            },
            validations: {
              ...this.state.validations,
              firstName: {validateStatus: 'success'},
              lastName: {validateStatus: 'success'},
            }
          })
        )
        .catch(reason => {
          notification.error({
            message: 'Bike Management',
            description: 'Error receiving user profile!',
          });
          console.log(reason);
        });
    }
  }

  getAvailableCategoriesToOption = () =>
    this.state.event.categories.map(cat => <Select.Option
      key={cat.id} value={cat.id}>{cat.name}</Select.Option>);

  isFormInvalid = () => {
    return this.state.validations.firstName.validateStatus === 'error' ||
      this.state.validations.lastName.validateStatus === 'error' ||
      this.state.validations.competitionNumber.validateStatus === 'error' ||
      this.state.validations.category.validateStatus === 'error';
  };

  validateName = (name) => {
    if (name.length < NAME_MIN_LENGTH) {
      return {
        validateStatus: 'error',
        errorMsg: `Name is too short (Minimum ${NAME_MIN_LENGTH} characters needed.)`
      }
    }
    if (name.length > NAME_MAX_LENGTH) {
      return {
        validateStatus: 'error',
        errorMsg: `Name is too long (Maximum ${NAME_MAX_LENGTH} characters allowed.)`
      }
    }
    return {
      validateStatus: 'success',
      errorMsg: null
    }
  };

  validateCategories = (cat) => {
    if (!this.state.categoriesInEvent.includes(cat)) {
      return {
        validateStatus: 'error',
        errorMsg: 'Must select a category.'
      }
    } else {
      return {
        validateStatus: 'success',
        errorMsg: null
      }
    }
  };

  validateCompetitionNumber = (num) => {
    if (num <= 0) {
      return {
        validateStatus: 'error',
        errorMsg: `Incorrect number! Must be larger than 0.`
      }
    }
    if (isNaN(num)) {
      return {
        validateStatus: 'error',
        errorMsg: `Incorrect number! Must be a number.`
      }
    }
    if (this.state.notAvailableNumbers.includes(parseInt(num, 10))) {
      return {
        validateStatus: 'error',
        errorMsg: `This number in not available! Please, try another number.`
      }
    }
    return {
      validateStatus: 'success',
      errorMsg: null
    }
  };

  handleSelectCategories = (selectedCat) => {
    let currState = this.state;
    currState.validations["category"] = this.validateCategories(selectedCat.label);
    currState.participant.categoryId = selectedCat.key;
    this.setState(currState)
  };

  handleInputChange = (ev, validationFun) => {
    const target = ev.target;
    const inputName = target.name;
    const inputValue = target.value;

    let currState = this.state;
    currState.validations[inputName] = validationFun(inputValue);
    currState.participant[inputName] = inputValue;
    this.setState(currState);
  };

  handleSubmit = (ev) => {
    ev.preventDefault();
    participateInEvent(this.state.participant)
      .then((response) => {
        notification.success({
          message: 'Success',
          description: 'You successfully register to event!',
        });
        this.props.history.push('/events/' + this.state.eventId);
      })
      .catch((error) => {
        notification.error({
          message: 'Failed',
          description: 'Something went wrong',
        });
        console.log(error);
      })
  };

  render() {
    console.log(this.state.event);
    return (

      <div className="wrap">
        <Skeleton active loading={this.state.eventLoading} paragraph={4}>
          <Card title={"Let's participate in " + this.state.event.name + ". Enter your personal data:"}>
            <Form onSubmit={this.handleSubmit}>
              <FormItem
                label="First Name"
                validateStatus={this.state.validations["firstName"].validateStatus}
                help={this.state.validations["firstName"].errorMsg}>
                <Input
                  size="large"
                  name="firstName"
                  autoComplete="off"
                  placeholder="Enter your first name"
                  value={this.state.participant.firstName}
                  onChange={(ev) => this.handleInputChange(ev, this.validateName)}/>
              </FormItem>
              <FormItem
                label="Last Name"
                validateStatus={this.state.validations["lastName"].validateStatus}
                help={this.state.validations["lastName"].errorMsg}>
                <Input
                  size="large"
                  name="lastName"
                  autoComplete="off"
                  placeholder="Enter your last name"
                  value={this.state.participant.lastName}
                  onChange={(ev) => this.handleInputChange(ev, this.validateName)}/>
              </FormItem>
              <FormItem
                label="Competition number"
                validateStatus={this.state.validations["competitionNumber"].validateStatus}
                help={this.state.validations["competitionNumber"].errorMsg}>
                <Input
                  size="large"
                  name="competitionNumber"
                  autoComplete="off"
                  placeholder="Enter your competition number"
                  value={this.state.participant.competitionNumber}
                  onChange={(ev) => this.handleInputChange(ev, this.validateCompetitionNumber)}/>
              </FormItem>
              <FormItem
                label="Categories"
                validateStatus={this.state.validations["category"].validateStatus}
                help={this.state.validations["category"].errorMsg}>
                <Select
                  mode="default"
                  labelInValue
                  placeholder="Select category"
                  showArrow={true}
                  onChange={this.handleSelectCategories}>
                  {this.getAvailableCategoriesToOption()}
                </Select>
              </FormItem>
              <FormItem>
                <Button type="primary" htmlType="submit" size="large"
                        className="signup-form-button"
                        disabled={this.isFormInvalid()}>Save
                </Button>
              </FormItem>
            </Form>
          </Card>
        </Skeleton>
      </div>


    );
  }
}

export default ParticipatePage;
