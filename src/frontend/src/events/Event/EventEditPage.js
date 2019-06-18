import React, {Component} from 'react'
import EventForm from "./EventForm";
import {editEvent, getEvent} from "../../util/api/APIUtils";
import {Button, notification} from 'antd'
import Link from "react-router-dom/es/Link";

class EventEditPage extends Component {
    constructor(props) {
        super();

        this.state = {
            eventId: props.match.params.id,
            loading: true,
            event: {},
            eventForm: <div>Loading...</div>
        };
    }

    componentWillMount() {
        getEvent(this.state.eventId)
            .then(response => {
                this.setState({
                    event: response.data,
                    loading: false,
                });
                this.setState({eventForm : <EventForm event={this.state.event} action={this.updateEvent}/>});
            })
            .catch(reason => {
                console.log(reason);
                this.setState({loading: false})
            });
    }

    updateEvent(editedEvent) {
        editEvent(editedEvent)
            .then((response) => {
                notification.success({
                    message: 'Bike Management',
                    description: 'Event was edited successfully!',
                });
            })
            .catch((error) => {
                let errorMessage = 'Sorry! Something went wrong. Please try again!';
                if (error.response && error.response.status === 401) {
                    errorMessage = 'You are not logged in!';
                }
                if (error.response && error.response.status === 403) {
                    errorMessage = 'You don\'t have permission to edit this event!';
                }
                notification.error({
                    message: 'Bike Management',
                    description: errorMessage,
                });
                console.log('Error updating event: ' + error)
            });
    }

    render() {
        return (<div className="signup-container">
            <h1 className="page-title">Edit event</h1>
            {this.state.eventForm}
            <Link to={"/events/" + this.state.eventId}>
                <Button
                    type="secondary"
                    htmlType="submit"
                    size="large"
                    style={{width: '100%'}}>
                    Cancel</Button>
            </Link>
        </div>)
    }
}

export default EventEditPage