import React, {Component} from 'react';
import {Link} from 'react-router-dom';
import {List} from 'antd';
import Event from "./Event/Event";
import './Events.css';

class Events extends Component {

  render() {
    return (
      <List
        className=""
        rowKey="id"
        grid={{gutter: 24, xxl: 4, xl: 3, lg: 3, md: 2, sm: 1, xs: 1}}
        dataSource={this.props.events}
        renderItem={item => (
          <List.Item key={item.id}>
            <Link to={"/events/" + item.id}>
              <Event data={item}/>
            </Link>
          </List.Item>
        )}
      />
    );
  }
}

export default Events;
