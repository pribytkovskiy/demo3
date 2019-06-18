import React from "react";
import {Col, Row} from "antd";
import Profile from "../../user/profile/Profile";
import UserEvents from "../../events/UserEvents/UserEvents";

const UserProfile = (props) => (
  <Row gutter={24} style={{paddingLeft: 24}}>
    <Col lg={7} md={24}>
      <Profile currentUser={props.currentUser}/>
    </Col>
    <Col lg={16} md={24} style={{backgroundColor: 'white', paddingBottom: 15}}>
      <UserEvents currentUser={props.currentUser}/>
    </Col>
  </Row>
);

export default UserProfile;