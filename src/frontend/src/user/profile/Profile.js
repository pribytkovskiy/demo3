import React, {Component} from "react";
import {Card, Skeleton} from "antd";
import './Profile.css';
import {getUserProfile} from "../../util/api/APIUtils";

//todo:avatar
class Profile extends Component {
  state = {
    user: null
  };

  componentDidMount() {
    getUserProfile(this.props.currentUser.id)
      .then(response => {
        this.setState({
          user: response.data
        })
      }).catch(error => console.log(error));
  }

  render() {
    return (
      <Skeleton loading={!this.state.user} active>
        {this.state.user ? (
          <Card bordered={false}>
            <div>
              <div className='avatarHolder'>
                <img alt="" src='https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png'/>
                <div className='name'>{this.state.user.firstName} {this.state.user.lastName}</div>
                <div className='detail'>
                  <p>{this.state.user.email}</p>
                  <p>{this.state.user.phoneNumber}</p>
                </div>
              </div>
            </div>
          </Card>
        ) : null}
      </Skeleton>
    );
  }
}

export default Profile;