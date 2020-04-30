import s from 'styled-components'

const Wrapper = s.div`
  padding: 2rem 5rem;
`

const Logo = s.div`
  float: left;
  margin-right: 5rem;
  font-size: 30px;
`

const Results = s.div`

`

const Home = () => {
  return (
    <Wrapper>
      <div>
        <Logo> PennSearch </Logo>
        <div class="field has-addons">
          <div class="control">
            <input class="input" type="text" placeholder="Search for anything @upenn" size="50" />
          </div>
          <div class="control">
            <a class="button is-info">
              Search
            </a>
          </div>
        </div>
      </div>
      <div>

      </div>
    </Wrapper>
  )
}

export default Home