import React, {Component} from 'react'
import {getUserProfile} from '../../../util/api/APIUtils'
import {Route, Switch} from 'react-router-dom'
import EditProfile from "../EditProfile/EditProfile";
import './AccountSettings.css'
import LoadingIndicator from "../../../common/LoadingIndicator";
import {Tabs} from 'antd';
import ChangePassword from "../ChangePassword";

const TabPane = Tabs.TabPane;

class AccountSettings extends Component {

  state = {
    profile: null
  }

  componentWillMount() {
    getUserProfile(this.props.userId)
      .then(response => {
        this.setState({
          profile: response.data
        })
      }).catch(error => console.log(error))
  }

  render() {
    return (
      this.state.profile ?
        <div className="settings-tabs">
          <Switch>
            <Tabs
              defaultActiveKey="1"
              tabPosition='left'>
              <TabPane tab="Basic" key="1">
                <Route render={() => <EditProfile
                  userId={this.props.userId}
                  firstName={this.state.profile.firstName}
                  lastName={this.state.profile.lastName}
                  email={this.state.profile.email}
                  phoneNumber={this.state.profile.phoneNumber}/>
                }/>
              </TabPane>

              <TabPane tab="Security" key="2">
                <Route render={() => <ChangePassword
                  userId={this.props.userId}/>
                }/>
              </TabPane>
            </Tabs>
          </Switch>
        </div>
        : <LoadingIndicator/>

    )
  }

}

export default AccountSettings