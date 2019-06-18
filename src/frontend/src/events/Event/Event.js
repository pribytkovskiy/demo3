import React from 'react';
import {Card} from 'antd';
import Moment from 'moment';
import './Event.css';
import placeholder from '../../assets/placeholder.jpg'

const Event = (props) => {

  return (
    <Card
      hoverable
      cover={<img alt={props.data.name} src={placeholder}/>}>
      <Card.Meta
        title={props.data.name}
        description={props.data.description}/>
      <div className="ant-card-meta-description ant-card-meta-detail event-card-categories">
        <span>{props.data.categories.map(category => category.name).join(', ')}</span>
      </div>
      <div className="ant-card-meta-description ant-card-meta-detail event-card-date-time">
        <span>{Moment(props.data.dateTime).format('MMMM Do YYYY, h:mm')}</span><br/>
      </div>
    </Card>
  )
};

export default Event;
