import React, { Component } from 'react'
import { Button, Form, Input, Select, notification } from 'antd'
import {NAME_MAX_LENGTH,
    NAME_MIN_LENGTH,
    TEXT_AREA_DEFAULT_MIN_LENGTH,
    TEXT_AREA_DEFAULT_MAX_LENGTH} from "../../constants/constants";
import TextArea from "antd/es/input/TextArea";
import {getAllCategories} from "../../util/api/APIUtils";

const FormItem = Form.Item;

class EventForm extends Component {
    constructor(props) {
        super();

        this.state = {
            event: {
                name: null,
                description: null,
                dateTime: null,
                categories: null
            },
            action: props.action,
            validations: {
                name: {},
                description: {},
                dateTime: {},
                categories: {}
            },
            categoryOptions: [],
            categoryOptionsNamesCurrent: [],
            availableCategories: []
        };

        if (props.event != null) {
            this.state.event = props.event;
            this.state.categoryOptionsNamesCurrent = props.event.categories.map(cat => cat.name)
        }
        this.getAllAvailableCategories();
    }

    handleInputChange = (ev, validationFun) => {
        const target = ev.target;
        const inputName = target.name;
        const inputValue = target.value;

        let currState = this.state;
        currState.validations[inputName] = validationFun(inputValue);
        currState.event[inputName] = inputValue;
        this.setState(currState);
    };

    handleChangeCategories = (selCatsNames) => {
        let cats = selCatsNames.map(cn => {
            let id = 0;
            this.state.availableCategories.forEach(ac => {
                if (ac.name === cn) {
                    id = ac.id
                }
            });
            return {'id': id, 'name': cn}
        });

        let currState = this.state;
        currState.validations.categories = this.validateCategories(selCatsNames);
        currState.event.categories = cats;
        this.setState(currState)
    };

    getAllAvailableCategories = () => {
        getAllCategories()
            .then((response) => {
                let opts = response.data.map(cat => <Select.Option key={cat.name}>{cat.name}</Select.Option>);
                this.setState({
                    categoryOptions: opts,
                    availableCategories: response.data
                })
            })
            .catch((error) => {
                notification.error({
                    message: 'Bike Management',
                    description: 'Error receiving available categories!',
                });
                console.log('Error receiving available categories: ' + error)
            })
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

    validateDescription = (name) => {
        if (name.length < TEXT_AREA_DEFAULT_MIN_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: `Description is too short (Minimum ${TEXT_AREA_DEFAULT_MIN_LENGTH} characters needed.)`
            }
        }
        if (name.length > TEXT_AREA_DEFAULT_MAX_LENGTH) {
            return {
                validateStatus: 'error',
                errorMsg: `Description is too long (Maximum ${TEXT_AREA_DEFAULT_MAX_LENGTH} characters allowed.)`
            }
        }
        return {
            validateStatus: 'success',
            errorMsg: null
        }
    };

    validateDate = (date) => {
        if (!date) {
            return {
                validateStatus: 'error',
                errorMsg: 'Date is incorrect.'
            }
        }
        if (new Date(date) < new Date()) {
            return {
                validateStatus: 'error',
                errorMsg: 'Date is in the past.'
            }
        }
        return {
            validateStatus: 'success',
            errorMsg: null
        }
    };

    validateCategories = (cats) => {
        if (cats.length < 1) {
            return {
                validateStatus: 'error',
                errorMsg: 'Must select at least one category.'
            }
        }
        return {
            validateStatus: 'success',
            errorMsg: null
        }
    };

    isFormInvalid = () => {
        for (let field in this.state.validations) {
            if (this.state.validations[field].validateStatus === 'error') {
                return true
            }
        }
        return false
    };

    handleSubmit = (ev) => {
        ev.preventDefault();
        this.state.action(this.state.event);
    };

    render() {
        return <div className="signup-container">
            <Form onSubmit={this.handleSubmit} className="signup-form">
                <FormItem
                    label="Name"
                    validateStatus={this.state.validations["name"].validateStatus}
                    help={this.state.validations["name"].errorMsg}>
                    <Input
                        size="large"
                        name="name"
                        autoComplete="off"
                        placeholder="Enter event name"
                        value={this.state.event.name}
                        onChange={(ev) => this.handleInputChange(ev, this.validateName)}/>
                </FormItem>
                <FormItem
                    label="Description"
                    validateStatus={this.state.validations["description"].validateStatus}
                    help={this.state.validations["description"].errorMsg}>
                    <TextArea
                        size="large"
                        name="description"
                        type="textarea"
                        autoComplete="off"
                        placeholder="Enter event description"
                        value={this.state.event.description}
                        onChange={(ev) => this.handleInputChange(ev, this.validateDescription)}/>
                </FormItem>
                <FormItem
                    label="Date"
                    hasFeedback
                    validateStatus={this.state.validations["dateTime"].validateStatus}
                    help={this.state.validations["dateTime"].errorMsg}>
                    <Input
                        size="large"
                        name="dateTime"
                        type="datetime-local"
                        autoComplete="off"
                        placeholder="Enter date and time"
                        value={this.state.event.dateTime}
                        onChange={(ev) => this.handleInputChange(ev, this.validateDate)}/>
                </FormItem>
                <FormItem
                    label="Categories"
                    validateStatus={this.state.validations["categories"].validateStatus}
                    help={this.state.validations["categories"].errorMsg}>
                <Select
                    mode="multiple"
                    style={{width: '100%'}}
                    placeholder="Select categories"
                    defaultValue={this.state.categoryOptionsNamesCurrent}
                    onChange={this.handleChangeCategories}>
                    {this.state.categoryOptions}
                </Select>
                </FormItem>
                <FormItem>
                    <Button
                        type="primary"
                        htmlType="submit"
                        size="large"
                        className="signup-form-button"
                        disabled={this.isFormInvalid()}>Save</Button>
                </FormItem>
            </Form>
        </div>
    }
}

export default EventForm