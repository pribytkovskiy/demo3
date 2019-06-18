import React, {Component} from 'react';
import {Avatar, Icon, List, Skeleton, Tag} from 'antd';
import {getEventPage, getParticipantByUserId} from "../../util/api/APIUtils";
import {Link} from "react-router-dom";
import Moment from 'moment';

class UserEvents extends Component {
  state = {
    userEvents: null,
    page: 1,
    pageSize: 2
  };

  loadEventPage = () => {
    getParticipantByUserId(this.props.currentUser.id)
      .then(response => {
        getEventPage(this.state.page, this.state.pageSize, {
          participantIds: response.data.map(participant => participant.id)
        }).then(eventPageResponse => {
          this.setState({
            userEvents: eventPageResponse.data
          })
        }).catch(error => console.log(error));
      }).catch(error => console.log(error));
  };

  componentDidMount() {
    this.loadEventPage()
  }

  componentDidUpdate(prevProps, prevState) {
    if (this.state.page !== prevState.page) {
      this.loadEventPage()
    }
  }

  getRandomColor = () => {
    const colors = [
      'magenta', 'red', 'volcano', 'orange', 'gold',
      'lime', 'green', 'cyan', 'blue', 'geekblue', 'purple'];
    const index = Math.floor(Math.random() * colors.length);
    return colors[index];
  };

  handleUnsubscribe = () => {
    //todo: unsubscribe
    console.log('unsubscribe');
  };

  getEventActions = (event) => {
    return [
      <span>
        <Link to={`/events/${event.id}`}>
          <Icon type="eye" style={{marginRight: 8}}/>
          Open event
        </Link>
      </span>,
      <span>
        <Link to={`/users/${event.managerId}`}>
          <Icon type="user" style={{marginRight: 8}}/>
          Go to manager
        </Link>
      </span>,
      <span onClick={this.handleUnsubscribe}>
        <Icon type="frown" style={{marginRight: 8}}/>
        Unsubscribe
      </span>
    ];
  };

  render() {
    return (
      <Skeleton loading={!this.state.userEvents} paragraph={{rows: 5}} active>
        {this.state.userEvents ? (
          <List
            itemLayout="vertical"
            size="large"
            pagination={{
              onChange: page => this.setState({page: page}),
              pageSize: this.state.pageSize,
              total: this.state.userEvents.totalElements
            }}
            dataSource={this.state.userEvents.content}
            header={<div><b>Subscribed Events</b></div>}
            renderItem={event => (
              <List.Item
                key={event.id}
                actions={this.getEventActions(event)}
                extra={<img width={272} alt="logo"
                            src="https://gw.alipayobjects.com/zos/rmsportal/mqaQswcyDLcXyDKnZfES.png"/>}
              >
                <List.Item.Meta
                  avatar={<Avatar src='https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png'/>}
                  title={<Link to={`/events/${event.id}`}>{event.name}</Link>}
                  description={`Starts at: ${Moment(event.dateTime).format('MMMM Do YYYY, h:mm')}`}
                />
                {event.description}
                <div style={{marginTop: 10}}>
                  {event.categories.map(category =>
                    <Tag key={category.id} color={this.getRandomColor()}>{category.name}</Tag>)}
                </div>
              </List.Item>
            )}
          />
        ) : null}
      </Skeleton>
    );
  }
}

export default UserEvents;
