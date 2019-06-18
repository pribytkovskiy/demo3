import React, {Component} from 'react';
import {Card, Col, notification, Row, Skeleton} from 'antd';
import {createEvent, getAllCategories} from "../../util/api/APIUtils";
import EventForm from "../../events/EventForm/EventForm";

class CreateEventPage extends Component {
  state = {
    categories: [],
    loading: true,
  };

  componentWillMount() {
    getAllCategories()
      .then(response => {
        this.setState({
          categories: response.data,
          loading: false,
        })
      })
      .catch(reason => {
        console.log(reason);
        this.setState({loading: false,});
      })
  }

  postEvent = (newEvent) => {
    createEvent(newEvent)
      .then((response) => {
        notification.success({
          message: 'Success',
          description: 'Event created',
        });
          this.props.history.push('/events');
      }).catch((error) => {
      notification.error({
        message: 'Failed',
        description: 'Something went wrong',
      });
      console.log(error);
    })
  };

  render() {
    return (
      <div className="bc-content-body">
        <Row gutter={24}>
          <Col span={24}>
            <Skeleton loading={this.state.loading} paragraph={5} active>
              <Card
                title="Create new event"
                bordered={false}>
                <EventForm
                  currentUser={this.props.currentUser}
                  categories={this.state.categories}
                  action={this.postEvent}/>
              </Card>
            </Skeleton>
          </Col>
        </Row>
      </div>
    );
  }
}

export default CreateEventPage;
